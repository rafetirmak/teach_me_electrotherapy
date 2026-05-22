package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy

import android.content.Context
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model.DictionaryEntry
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils.DictionarySyncManager
import java.text.Collator
import java.util.Locale

object DictionaryDataProvider {
    fun getDictionary(context: Context, isTurkish: Boolean): List<DictionaryEntry> {
        val lang = if (isTurkish) "tr" else "en"
        val locale = if (isTurkish) Locale("tr", "TR") else Locale.US
        val collator = Collator.getInstance(locale)
        
        val localData = DictionarySyncManager.getLocalDictionary(context, lang)
        
        if (localData != null) {
            return localData.sortedWith(compareBy(collator) { it.term })
        }

        val defaultList = if (isTurkish) {
            listOf(
                DictionaryEntry("Akım Yoğunluğu", "Birim elektrot alanına düşen akım miktarıdır (mA/cm²)."),
                DictionaryEntry("Amper (Akım Şiddeti)", "Birim zamanda geçen elektrik yükü miktarıdır. Fizyolojik etkiyi belirleyen temel parametredir."),
                DictionaryEntry("Anot", "Pozitif yüklü elektrottur."),
                DictionaryEntry("Dalga Formu (Waveform)", "Elektrik akımının zaman içindeki değişimini gösteren grafiksel şekildir (Sinüs, kare, üçgen vb.)."),
                DictionaryEntry("Darbe Süresi (Pulse Duration)", "Tek bir elektrik darbesinin ne kadar sürdüğünü ifade eder. Genellikle mikrosaniye (µs) veya milisaniye (ms) ile ölçülür."),
                DictionaryEntry("Duty Cycle (Görev Döngüsü)", "Akımın aktif olduğu sürenin toplam periyoda oranıdır."),
                DictionaryEntry("Elektrot", "Elektrik akımını cihazdan vücuda ileten temas yüzeyidir."),
                DictionaryEntry("Faradik Akım", "Kısa süreli (0.1 - 1 ms) ve asimetrik bifazik impulslardan oluşan bir akım türüdür."),
                DictionaryEntry("Frekans (Frequency)", "Saniyedeki tekrarlanan darbe veya döngü sayısıdır. Birimi Hertz (Hz) 'dir."),
                DictionaryEntry("Galvanik Akım", "Dokuya kesintisiz ve tek yönlü uygulanan doğru akımdır."),
                DictionaryEntry("Genlik (Amplitude)", "Akımın şiddetini veya voltajın büyüklüğünü ifade eder. Genellikle Amper (mA) veya Voltaj (V) cinsinden ölçülür."),
                DictionaryEntry("HVPS", "Yüksek Voltaj Pulsatili Stimülasyon. Genellikle ödem kontrolü ve yara iyileşmesi için kullanılan çift tepeli monofazik bir akımdır."),
                DictionaryEntry("İnterferansiyel Akım (IFC)", "İki orta frekanslı akımın doku içinde kesişerek alçak frekanslı bir girişim oluşturması prensibine dayanır."),
                DictionaryEntry("İyontoferez", "Galvanik akım kullanarak ilaç moleküllerinin deri yoluyla dokuya transfer edilmesidir."),
                DictionaryEntry("Katot", "Negatif yüklü elektrottur."),
                DictionaryEntry("NMES", "Nöromüsküler Elektriksel Stimülasyon. Kasları güçlendirmek veya rehabilite etmek için kullanılan bir yöntemdir."),
                DictionaryEntry("Osiloskop", "Elektriksel sinyallerin dalga formunu görselleştiren ve zaman içindeki değişimini izlemeyi sağlayan cihazdır."),
                DictionaryEntry("Sinyal", "Bilgi taşıyan veya dokuyu uyarmak amacıyla üretilen elektriksel dalgalanmadır."),
                DictionaryEntry("TENS", "Transkutanöz Elektriksel Sinir Stimülasyonu. Genellikle ağrı kontrolü için kullanılan bir akım türüdür."),
                DictionaryEntry("Voltaj (Gerilim)", "Elektrik akımını hareket ettiren potansiyel farktır; elektroterapide doku direncini yenmek için gereken itici gücü temsil eder.")
            )
        } else {
            listOf(
                DictionaryEntry("Amplitude", "Refers to the intensity of the current or the magnitude of the voltage. Usually measured in Amperes (mA) or Voltage (V)."),
                DictionaryEntry("Ampere (Current Intensity)", "The amount of electric charge passing through a point per unit of time. It is the primary parameter determining the physiological effect."),
                DictionaryEntry("Anode", "The positively charged electrode."),
                DictionaryEntry("Cathode", "The negatively charged electrode."),
                DictionaryEntry("Current Density", "The amount of current per unit electrode area (mA/cm²)."),
                DictionaryEntry("Duty Cycle", "The ratio of the time the current is active to the total period."),
                DictionaryEntry("Electrode", "The contact surface that transmits electrical current from the device to the body."),
                DictionaryEntry("Faradic Current", "A type of current consisting of short-duration (0.1 - 1 ms) and asymmetric biphasic pulses."),
                DictionaryEntry("Frequency", "The number of repeated pulses or cycles per second. Its unit is Hertz (Hz)."),
                DictionaryEntry("Galvanic Current", "A direct current applied to the tissue continuously and unidirectionally."),
                DictionaryEntry("High Voltage Pulsed Stimulation (HVPS)", "A twin-peaked monophasic current usually used for edema control and wound healing."),
                DictionaryEntry("Interferential Current (IFC)", "Based on the principle of two medium-frequency currents crossing in the tissue to create a low-frequency interference."),
                DictionaryEntry("Iontophoresis", "Transfer of drug molecules into the tissue through the skin using galvanic current."),
                DictionaryEntry("Neuromuscular Electrical Stimulation (NMES)", "A method used to strengthen or rehabilitate muscles."),
                DictionaryEntry("Oscilloscope", "A device that visualizes the waveform of electrical signals and allows monitoring their change over time."),
                DictionaryEntry("Pulse Duration", "Refers to how long a single electrical pulse lasts. Usually measured in microseconds (µs) or milliseconds (ms)."),
                DictionaryEntry("Signal", "An electrical fluctuation generated to carry information or to stimulate tissue."),
                DictionaryEntry("TENS", "Transcutaneous Electrical Nerve Stimulation. A type of current usually used for pain control."),
                DictionaryEntry("Voltage", "The potential difference that moves the electric current; in electrotherapy, it represents the driving force required to overcome tissue resistance."),
                DictionaryEntry("Waveform", "The graphical shape representing the change of electrical current over time (Sine, square, triangular, etc.).")
            )
        }
        
        return defaultList.sortedWith(compareBy(collator) { it.term })
    }
}
