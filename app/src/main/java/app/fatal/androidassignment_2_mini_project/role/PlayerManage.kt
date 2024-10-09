package app.fatal.androidassignment_2_mini_project.role

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class PlayerManage {

    private val tag = "players"
    private val collectionName = "players"
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun savePlayer (uid: String, callback: ()-> Unit) {
        val player = hashMapOf(
            "player" to uid
        )
        fireStore.collection(collectionName)
            .add(player)
            .addOnSuccessListener { callback() }
            .addOnFailureListener {
                Log.d(tag, it.toString())
            }
    }

    fun findPlayer (uid: String, onSuccess: () -> Unit, onFailed: ()-> Unit) {
        fireStore.collection(collectionName)
            .whereEqualTo("player", uid).get()
            .addOnSuccessListener {
                if (it.documents.size > 0) {
                    onSuccess()
                } else {
                    onFailed()
                }
            }
            .addOnFailureListener {
                Log.d(tag, it.toString())
            }
    }

}