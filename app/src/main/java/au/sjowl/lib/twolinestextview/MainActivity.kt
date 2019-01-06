package au.sjowl.lib.twolinestextview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        triangleView.setOnTouchListener { v, event ->
            v.invalidate()
            false
        }

//        textView.underlines = "woman guard security of hospital he found him <63χρονο> unconscious in entrance and right away call aid".split(" ")
//        textView.text = "Σύμφωνα με την Αστυνομία, γυναίκα φρουρός ασφαλείας του νοσοκομείου εντόπισε τον 63χρονο αναίσθητο στην είσοδο και αμέσως κάλεσε βοήθεια".split(" ")
    }
}
