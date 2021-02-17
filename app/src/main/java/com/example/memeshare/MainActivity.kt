package com.example.memeshare
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeshare.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener
{
    //android:background="#000000"
    private lateinit var binding: ActivityMainBinding
    var currentImageUrl:String?=null

    var subReddit=""
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setContentView(binding.root)
        loadMeme()
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long)
    {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        //var temp= spin.get(pos).toString()
    }
    override fun onNothingSelected(parent: AdapterView<*>)
    {
        // Another interface callback
    }

    public fun ddMenu()
    {
        val spinner: Spinner = findViewById(R.id.menu)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_iteams,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

    }
    private fun loadMeme()
    {
        val spin=findViewById<Spinner>(R.id.menu)
        spin.onItemSelectedListener=this
        subReddit=spin.selectedItem.toString()

        val pb=findViewById<ProgressBar>(R.id.progressBar)
        pb.visibility=View.VISIBLE
        // Instantiate the RequestQueue.
        //val queue = Volley.newRequestQueue(this)
        val imageView=findViewById<ImageView>(R.id.memeImageView)
        if(subReddit=="Random")
            subReddit=""
        var url:String="https://meme-api.herokuapp.com/gimme/$subReddit"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    //val nsfw: JSONObject = response.getJSONObject("nsfw")
                    //nsfw.put("nsfw",true)
                    currentImageUrl = response.getString("url")

                    Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                        ): Boolean {
                            pb.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                        ): Boolean {
                            pb.visibility = View.GONE
                            return false

                        }
                    }).into(imageView)
                },
                { Toast.makeText(this, "Some Error has Occurred", Toast.LENGTH_LONG).show() })

        // Add the request to the RequestQueue.
       MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun nextMeme(view: View)
    {
        loadMeme()
    }
    fun shareMeme(view: View)
    {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey Checkout this meme from Reddit $currentImageUrl")
        val chooser=Intent.createChooser(intent, "Send this meme via...")
        startActivity(chooser)
    }
}

