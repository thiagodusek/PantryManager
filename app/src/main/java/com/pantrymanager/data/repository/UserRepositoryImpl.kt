package com.pantrymanager.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.pantrymanager.domain.entity.User
import com.pantrymanager.domain.entity.Address
import com.pantrymanager.domain.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : UserRepository {
    
    override suspend fun registerUser(user: User, password: String): Result<User> {
        return try {
            // Create user with email and password
            val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Falha ao criar usuário")
            
            // Save user data to Firestore
            val userMap = mapOf(
                "id" to user.id,
                "nome" to user.nome,
                "sobrenome" to user.sobrenome,
                "email" to user.email,
                "cpf" to user.cpf,
                "endereco" to mapOf(
                    "endereco" to user.endereco.endereco,
                    "numero" to user.endereco.numero,
                    "complemento" to user.endereco.complemento,
                    "cep" to user.endereco.cep,
                    "cidade" to user.endereco.cidade,
                    "estado" to user.endereco.estado,
                    "latitude" to user.endereco.latitude,
                    "longitude" to user.endereco.longitude
                ),
                "login" to user.login,
                "searchRadius" to user.searchRadius,
                "nfePermissions" to user.nfePermissions
            )
            
            firebaseFirestore.collection("users")
                .document(firebaseUser.uid)
                .set(userMap)
                .await()
            
            Result.success(user.copy(id = firebaseUser.uid))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Usuário não encontrado")
            
            // Get user data from Firestore
            val userDoc = firebaseFirestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
            
            if (userDoc.exists()) {
                val userData = userDoc.data!!
                val endereco = userData["endereco"] as? Map<String, Any> ?: emptyMap()
                val user = User(
                    id = userData["id"] as? String ?: firebaseUser.uid,
                    nome = userData["nome"] as? String ?: "",
                    sobrenome = userData["sobrenome"] as? String ?: "",
                    email = userData["email"] as? String ?: email,
                    cpf = userData["cpf"] as? String ?: "",
                    endereco = Address(
                        endereco = endereco["endereco"] as? String ?: "",
                        numero = endereco["numero"] as? String ?: "",
                        complemento = endereco["complemento"] as? String,
                        cep = endereco["cep"] as? String ?: "",
                        cidade = endereco["cidade"] as? String ?: "",
                        estado = endereco["estado"] as? String ?: "",
                        latitude = endereco["latitude"] as? Double,
                        longitude = endereco["longitude"] as? Double
                    ),
                    login = userData["login"] as? String ?: email,
                    searchRadius = (userData["searchRadius"] as? Double) ?: 10.0,
                    nfePermissions = (userData["nfePermissions"] as? Boolean) ?: false
                )
                Result.success(user)
            } else {
                // Create basic user data if doesn't exist
                val user = User(
                    id = firebaseUser.uid,
                    nome = firebaseUser.displayName?.split(" ")?.firstOrNull() ?: "",
                    sobrenome = firebaseUser.displayName?.split(" ")?.drop(1)?.joinToString(" ") ?: "",
                    email = firebaseUser.email ?: email,
                    cpf = "",
                    endereco = Address(
                        endereco = "",
                        numero = "",
                        complemento = null,
                        cep = "",
                        cidade = "",
                        estado = "",
                        latitude = null,
                        longitude = null
                    ),
                    login = firebaseUser.email ?: email,
                    searchRadius = 10.0,
                    nfePermissions = false
                )
                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user ?: throw Exception("Falha no login com Google")
            
            // Check if user already exists in Firestore
            val userDoc = firebaseFirestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
            
            val user = if (userDoc.exists()) {
                // User exists, get from Firestore
                val userData = userDoc.data!!
                val endereco = userData["endereco"] as? Map<String, Any> ?: emptyMap()
                User(
                    id = userData["id"] as? String ?: firebaseUser.uid,
                    nome = userData["nome"] as? String ?: "",
                    sobrenome = userData["sobrenome"] as? String ?: "",
                    email = userData["email"] as? String ?: firebaseUser.email ?: "",
                    cpf = userData["cpf"] as? String ?: "",
                    endereco = Address(
                        endereco = endereco["endereco"] as? String ?: "",
                        numero = endereco["numero"] as? String ?: "",
                        complemento = endereco["complemento"] as? String,
                        cep = endereco["cep"] as? String ?: "",
                        cidade = endereco["cidade"] as? String ?: "",
                        estado = endereco["estado"] as? String ?: "",
                        latitude = endereco["latitude"] as? Double,
                        longitude = endereco["longitude"] as? Double
                    ),
                    login = userData["login"] as? String ?: firebaseUser.email ?: "",
                    searchRadius = (userData["searchRadius"] as? Double) ?: 10.0,
                    nfePermissions = (userData["nfePermissions"] as? Boolean) ?: false
                )
            } else {
                // New user, create in Firestore
                val newUser = User(
                    id = firebaseUser.uid,
                    nome = firebaseUser.displayName?.split(" ")?.firstOrNull() ?: "",
                    sobrenome = firebaseUser.displayName?.split(" ")?.drop(1)?.joinToString(" ") ?: "",
                    email = firebaseUser.email ?: "",
                    cpf = "",
                    endereco = Address(
                        endereco = "",
                        numero = "",
                        complemento = null,
                        cep = "",
                        cidade = "",
                        estado = "",
                        latitude = null,
                        longitude = null
                    ),
                    login = firebaseUser.email ?: "",
                    searchRadius = 10.0,
                    nfePermissions = false
                )
                
                // Save to Firestore
                val userMap = mapOf(
                    "id" to newUser.id,
                    "nome" to newUser.nome,
                    "sobrenome" to newUser.sobrenome,
                    "email" to newUser.email,
                    "cpf" to newUser.cpf,
                    "endereco" to mapOf(
                        "endereco" to newUser.endereco.endereco,
                        "numero" to newUser.endereco.numero,
                        "complemento" to newUser.endereco.complemento,
                        "cep" to newUser.endereco.cep,
                        "cidade" to newUser.endereco.cidade,
                        "estado" to newUser.endereco.estado,
                        "latitude" to newUser.endereco.latitude,
                        "longitude" to newUser.endereco.longitude
                    ),
                    "login" to newUser.login,
                    "searchRadius" to newUser.searchRadius,
                    "nfePermissions" to newUser.nfePermissions
                )
                
                firebaseFirestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(userMap)
                    .await()
                
                newUser
            }
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Flow<User?> {
        return callbackFlow {
            val authListener = FirebaseAuth.AuthStateListener { auth ->
                val firebaseUser = auth.currentUser
                if (firebaseUser != null) {
                    // User is signed in, get user data from Firestore
                    firebaseFirestore.collection("users")
                        .document(firebaseUser.uid)
                        .get()
                        .addOnSuccessListener { userDoc ->
                            if (userDoc.exists()) {
                                val userData = userDoc.data!!
                                val endereco = userData["endereco"] as? Map<String, Any> ?: emptyMap()
                                val user = User(
                                    id = userData["id"] as? String ?: firebaseUser.uid,
                                    nome = userData["nome"] as? String ?: "",
                                    sobrenome = userData["sobrenome"] as? String ?: "",
                                    email = userData["email"] as? String ?: firebaseUser.email ?: "",
                                    cpf = userData["cpf"] as? String ?: "",
                                    endereco = Address(
                                        endereco = endereco["endereco"] as? String ?: "",
                                        numero = endereco["numero"] as? String ?: "",
                                        complemento = endereco["complemento"] as? String,
                                        cep = endereco["cep"] as? String ?: "",
                                        cidade = endereco["cidade"] as? String ?: "",
                                        estado = endereco["estado"] as? String ?: "",
                                        latitude = endereco["latitude"] as? Double,
                                        longitude = endereco["longitude"] as? Double
                                    ),
                                    login = userData["login"] as? String ?: firebaseUser.email ?: "",
                                    searchRadius = (userData["searchRadius"] as? Double) ?: 10.0,
                                    nfePermissions = (userData["nfePermissions"] as? Boolean) ?: false
                                )
                                trySend(user)
                            } else {
                                trySend(null)
                            }
                        }
                        .addOnFailureListener {
                            trySend(null)
                        }
                } else {
                    trySend(null)
                }
            }
            
            firebaseAuth.addAuthStateListener(authListener)
            
            awaitClose {
                firebaseAuth.removeAuthStateListener(authListener)
            }
        }
    }

    override suspend fun updateUser(user: User): Result<User> {
        // TODO: Implement user update
        return Result.failure(Exception("Not implemented yet"))
    }

    override suspend fun deleteUser(): Result<Unit> {
        // TODO: Implement user deletion
        return Result.failure(Exception("Not implemented yet"))
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        // TODO: Implement password change
        return Result.failure(Exception("Not implemented yet"))
    }

    override suspend fun isEmailAvailable(email: String): Result<Boolean> {
        // TODO: Check if email is available
        return Result.success(true)
    }

    override suspend fun isCpfAvailable(cpf: String): Result<Boolean> {
        // TODO: Check if CPF is available
        return Result.success(true)
    }
}
