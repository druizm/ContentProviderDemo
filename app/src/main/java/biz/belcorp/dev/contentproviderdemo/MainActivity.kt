package biz.belcorp.dev.contentproviderdemo

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


class MainActivity : AppCompatActivity() {

    companion object {

        // QAS
        const val CP_ESIKA = "biz.belcorp.consultoras.esika.develop.provider.data"
        const val CP_LBEL = "biz.belcorp.consultoras.lbel.develop.provider.data"

        // BETA PRODUCTION
        //const val CP_ESIKA = "biz.belcorp.consultoras.esika.stage.provider.data"
        //const val CP_LBEL = "biz.belcorp.consultoras.lbel.stage.provider.data"

        const val PERMISSION_ESIKA = "$CP_ESIKA.READ_DATABASE"
        const val PERMISSION_LBEL = "$CP_LBEL.READ_DATABASE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnUserEsika.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, PERMISSION_ESIKA) !=
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_ESIKA), 1)
            } else {
                val contentUri = Uri.parse("content://$CP_ESIKA/User")
                contentUser(contentUri)
            }
        }

        btnClientsEsika.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, PERMISSION_ESIKA) !=
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_ESIKA), 1)
            } else {
                val contentUri = Uri.parse("content://$CP_ESIKA/Client")
                contentClients(contentUri)
            }
        }

        btnUserLBel.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, PERMISSION_LBEL) !=
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_LBEL), 1)
            } else {
                val contentUri = Uri.parse("content://$CP_LBEL/User")
                contentClients(contentUri)
            }
        }

        btnClientsLBel.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, PERMISSION_LBEL) !=
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_LBEL), 1)
            } else {
                val contentUri = Uri.parse("content://$CP_LBEL/Client")
                contentUser(contentUri)
            }
        }
    }

    /**  functions  **/

    private fun showToastMessage (msg : String) {
        Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
    }

    private fun contentUser(contentUri: Uri) {
        val yourCR = contentResolver.acquireContentProviderClient(contentUri)

        val cursor = yourCR.query(contentUri , null,
            null, null, null)

        if(cursor != null) {

            val cc = cursor.count

            Log.d("Content Provider ", "Usuario Contador $cc")

            if (cc > 0) {
                cursor.moveToFirst()
                // Loop in the cursor to get each row.
                do {

                    // Get column 4 value.
                    val column4Index = cursor.getColumnIndexOrThrow("ConsultantCode")
                    val column4Value = cursor.getString(column4Index)

                    // Get column 5 value.
                    val column5Index = cursor.getColumnIndex("ConsultantName")
                    val column5Value = cursor.getString(column5Index)


                    // Get column 3 value.
                    val column3Index = cursor.getColumnIndex("Campaing")
                    val column3Value = cursor.getString(column3Index)

                    showToastMessage(
                        "Code: $column4Value,\nName: $column5Value, \nCampaing: $column3Value")

                } while (cursor.moveToNext())
            }

            cursor.close()
        }
    }

    private fun contentClients(contentUri: Uri) {
        val cursor = contentResolver.query(
            contentUri, null,
            null, null, null
        )

        if (cursor != null) {

            val cc = cursor.count

            Log.d("Content Provider: ", "Clientes Contador $cc")

            if (cc > 0) {

                var array = ""

                cursor.moveToFirst()
                // Loop in the cursor to get each row.
                do {

                    // Get column 1 value.
                    val column1Index = cursor.getColumnIndex("ClienteID")
                    val column1Value = cursor.getString(column1Index)

                    // Get column 2 value.
                    val column2Index = cursor.getColumnIndex("Nombres")
                    val column2Value = cursor.getString(column2Index)

                    array += "Código: $column1Value, Name: $column2Value\n"


                } while (cursor.moveToNext())

                showToastMessage(array)

            }
            cursor.close()
        }
    }
}
