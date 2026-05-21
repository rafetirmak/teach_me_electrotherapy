package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy

import android.content.Context
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model.DictionaryEntry
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils.DictionarySyncManager

object DictionaryDataProvider {
    fun getDictionary(context: Context, isTurkish: Boolean): List<DictionaryEntry> {
        val lang = if (isTurkish) "tr" else "en"
        val localData = DictionarySyncManager.getLocalDictionary(context, lang)
        
        if (localData != null) return localData.sortedBy { it.term }

        return if (isTurkish) {
            listOf(
                DictionaryEntry("Genlik (Amplitude)", "Akımın şiddetini veya voltajın büyüklüğünü ifade eder. Genellikle Amper (mA) veya Voltaj (V) cinsinden ölçülür."),
                DictionaryEntry("Frekans (Frequency)", "Saniyedeki tekrarlanan darbe veya döngü sayısıdır. Birimi Hertz (Hz) 'dir."),
                DictionaryEntry("TENS", "Transkutanöz Elektriksel Sinir Stimülasyonu. Genellikle ağrı kontrolü için kullanılan bir akım türüdür."),
                DictionaryEntry("İnterferansiyel Akım (IFC)", "İki orta frekanslı akımın doku içinde kesişerek alçak frekanslı bir girişim oluşturması prensibine dayanır."),
                DictionaryEntry("Galvanik Akım", "Dokuya kesintisiz ve tek yönlü uygulanan doğru akımdır."),
                DictionaryEntry("Faradik Akım", "Kısa süreli (0.1 - 1 ms) ve asimetrik bifazik impulslardan oluşan bir akım türüdür."),
                DictionaryEntry("Darbe Süresi (Pulse Duration)", "Tek bir elektrik darbesinin ne kadar sürdüğünü ifade eder. Genellikle mikrosaniye (µs) veya milisaniye (ms) ile ölçülür."),
                DictionaryEntry("Duty Cycle (Görev Döngüsü)", "Akımın aktif olduğu sürenin toplam periyoda oranıdır."),
                DictionaryEntry("İyontoferez", "Galvanik akım kullanarak ilaç moleküllerinin deri yoluyla dokuya transfer edilmesidir."),
                DictionaryEntry("Elektrot", "Elektrik akımını cihazdan vücuda ileten temas yüzeyidir."),
                DictionaryEntry("NMES", "Nöromüsküler Elektriksel Stimülasyon. Kasları güçlendirmek veya rehabilite etmek için kullanılan bir yöntemdir."),
                DictionaryEntry("HVPS", "Yüksek Voltaj Pulsatili Stimülasyon. Genellikle ödem kontrolü ve yara iyileşmesi için kullanılan çift tepeli monofazik bir akımdır."),
                DictionaryEntry("Akım Yoğunluğu", "Birim elektrot alanına düşen akım miktarıdır (mA/cm²)."),
                DictionaryEntry("Katot", "Negatif yüklü elektrottur."),
                DictionaryEntry("Anot", "Pozitif yüklü elektrottur."),
                DictionaryEntry("Dalga Formu (Waveform)", "Elektrik akımının zaman içindeki değişimini gösteren grafiksel şekildir (Sinüs, kare, üçgen vb.)."),
                DictionaryEntry("Osiloskop", "Elektriksel sinyallerin dalga formunu görselleştiren ve zaman içindeki değişimini izlemeyi sağlayan cihazdır."),
                DictionaryEntry("Voltaj (Gerilim)", "Elektrik akımını hareket ettiren potansiyel farktır; elektroterapide doku direncini yenmek için gereken itici gücü temsil eder."),
                DictionaryEntry("Amper (Akım Şiddeti)", "Birim zamanda geçen elektrik yükü miktarıdır. Fizyolojik etkiyi belirleyen temel parametredir."),
                DictionaryEntry("Sinyal", "Bilgi taşıyan veya dokuyu uyarmak amacıyla üretilen elektriksel dalgalanmadır.")
            )
        } else {
            listOf(
                DictionaryEntry("Amplitude", "Refers to the intensity of the current or the magnitude of the voltage. Usually measured in Amperes (mA) or Voltage (V)."),
                DictionaryEntry("Frequency", "The number of repeated pulses or cycles per second. Its unit is Hertz (Hz)."),
                DictionaryEntry("TENS", "Transcutaneous Electrical Nerve Stimulation. A type of current usually used for pain control."),
                DictionaryEntry("Interferential Current (IFC)", "Based on the principle of two medium-frequency currents crossing in the tissue to create a low-frequency interference."),
                DictionaryEntry("Galvanic Current", "A direct current applied to the tissue continuously and unidirectionally."),
                DictionaryEntry("Faradic Current", "A type of current consisting of short-duration (0.1 - 1 ms) and asymmetric biphasic pulses."),
                DictionaryEntry("Pulse Duration", "Refers to how long a single electrical pulse lasts. Usually measured in microseconds (µs) or milliseconds (ms)."),
                DictionaryEntry("Duty Cycle", "The ratio of the time the current is active to the total period."),
                DictionaryEntry("Iontophoresis", "Transfer of drug molecules into the tissue through the skin using galvanic current."),
                DictionaryEntry("Electrode", "The contact surface that transmits electrical current from the device to the body."),
                DictionaryEntry("NMES", "Neuromuscular Electrical Stimulation. A method used to strengthen or rehabilitate muscles."),
                DictionaryEntry("HVPS", "High Voltage Pulsed Stimulation. A twin-peaked monophasic current usually used for edema control and wound healing."),
                DictionaryEntry("Current Density", "The amount of current per unit electrode area (mA/cm²)."),
                DictionaryEntry("Cathode", "The negatively charged electrode."),
                DictionaryEntry("Anode", "The positively charged electrode."),
                DictionaryEntry("Waveform", "The graphical shape representing the change of electrical current over time (Sine, square, triangular, etc.)."),
                DictionaryEntry("Oscilloscope", "A device that visualizes the waveform of electrical signals and allows monitoring their change over time."),
                DictionaryEntry("Voltage", "The potential difference that moves the electric current; in electrotherapy, it represents the driving force required to overcome tissue resistance."),
                DictionaryEntry("Ampere (Current Intensity)", "The amount of electric charge passing through a point per unit of time. It is the primary parameter determining the physiological effect."),
                DictionaryEntry("Signal", "An electrical fluctuation generated to carry information or to stimulate tissue.")
            )
        }.sortedBy { it.term }
    }
}
