package biz.belcorp.dev.contentproviderdemo

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.ContentProviderClient
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


class MainActivity : AppCompatActivity() {

    var PERMISSIONS = arrayOf(
        "biz.belcorp.consultoras.esika.dev.provider.data.READ_DATABASE"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnUserEsika.setOnClickListener {
            val contentUri = Uri.parse("content://biz.belcorp.maquillador.provider.data/Pedidos")
            // EXPLICIT INTENT EXAMPLE
            grantUriPermission("biz.belcorp.maquillador", contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)


            contentUser(contentUri)
        }

        btnClientsEsika.setOnClickListener {
            val contentUri = Uri.parse("content://biz.belcorp.consultoras.esika.provider.data/User")
            contentUser(contentUri)
        }

        btnUserLBel.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this,
                    "biz.belcorp.consultoras.esika.dev.provider.data.READ_DATABASE") !=
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
            } else {
                val contentUri = Uri.parse("content://biz.belcorp.consultoras.esika.dev.provider.data/User")
                contentUser(contentUri)
            }

        }

        btnClientsLBel.setOnClickListener {
            val contentUri = Uri.parse("content://biz.belcorp.consultoras.esika.dev.provider.data/Client")
            contentClients(contentUri)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

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
                    val column4Index = cursor.getColumnIndexOrThrow("Descripcion")
                    val column4Value = cursor.getString(column4Index)

                    // Get column 5 value.
                    val column5Index = cursor.getColumnIndex("MarcaDescripcion")
                    val column5Value = cursor.getString(column5Index)


                    // Get column 3 value.
                    val column3Index = cursor.getColumnIndex("Cantidad")
                    val column3Value = cursor.getString(column3Index)

                    showToastMessage(
                        "Descripcion: $column4Value,\n MarcaDescripcion: $column5Value, \nCantidad: $column3Value")

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
