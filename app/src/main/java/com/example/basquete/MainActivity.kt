package com.example.basquete

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private var pontuacaoTimeA: Int = 0
    private var periodo: Int = 0
    private var pontuacaoTimeB: Int = 0
    private var metricas: IntArray = IntArray(8)
    private lateinit var pTimeATextView: TextView
    private lateinit var pTimeBTextView: TextView
    private lateinit var periodoTextView: TextView
    private lateinit var tempoJogoTextView: TextView
    private lateinit var iniciarJogoButton: Button
    private lateinit var reiniciarJogoButton: Button
    private lateinit var metricasJogoButton: Button
    private lateinit var pausarTempoButton: Button

    private var countDownTimer: CountDownTimer? = null
    private var isTimerRodando = false
    private var isGameStarted = false
    private val tempoDoPeriodoEmMilissegundos: Long = 10 * 1000 //dps coloca 10 minutos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pTimeATextView = findViewById(R.id.placarTimeA)
        pTimeBTextView = findViewById(R.id.placarTimeB)
        periodoTextView = findViewById(R.id.periodo)

        val bTresPontosTimeA: Button = findViewById(R.id.tresPontosA)
        val bDoisPontosTimeA: Button = findViewById(R.id.doisPontosA)
        val bTLivreTimeA: Button = findViewById(R.id.tiroLivreA)
        val bTresPontosTimeB: Button = findViewById(R.id.tresPontosB)
        val bDoisPontosTimeB: Button = findViewById(R.id.doisPontosB)
        val bTLivreTimeB: Button = findViewById(R.id.tiroLivreB)

        tempoJogoTextView = findViewById(R.id.tempoJogo)
        iniciarJogoButton = findViewById(R.id.iniciarJogo)
        reiniciarJogoButton = findViewById(R.id.reiniciarPartida)
        pausarTempoButton = findViewById(R.id.btnPause)
        metricasJogoButton = findViewById(R.id.verMetricas)

        iniciarJogoButton.setOnClickListener {
            if (!isGameStarted) {
                reiniciarPartida()
                isGameStarted = true
            }
            if(periodo != 0){
                metricas[(periodo - 1) * 2] = pontuacaoTimeA
                metricas[((periodo - 1) * 2)+1] = pontuacaoTimeB
            }
            periodo += 1
            periodoTextView.setText(periodo.toString())
            iniciarTimer()
        }
        reiniciarJogoButton.setOnClickListener {
            isGameStarted = false
            iniciarJogoButton.text = "Iniciar Novo Jogo"
            reiniciarPartida()
        }
        pausarTempoButton.setOnClickListener {
            //add logica
        }
        metricasJogoButton.setOnClickListener {
            exibirDialogoDeMetricas()
        }
        bTresPontosTimeA.setOnClickListener {
            adicionarPontos(3, "A")
        }
        bDoisPontosTimeA.setOnClickListener {
            adicionarPontos(2, "A")
        }
        bTLivreTimeA.setOnClickListener {
            adicionarPontos(1, "A")
        }
        bTresPontosTimeB.setOnClickListener {
            adicionarPontos(3, "B")
        }
        bDoisPontosTimeB.setOnClickListener {
            adicionarPontos(2, "B")
        }
        bTLivreTimeB.setOnClickListener {
            adicionarPontos(1, "B")
        }
    }

    private fun iniciarTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(tempoDoPeriodoEmMilissegundos, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val segundosRestantes = millisUntilFinished / 1000
                val minutos = segundosRestantes / 60
                val segundos = segundosRestantes % 60
                val tempoFormatado = String.format("%02d:%02d", minutos, segundos)
                tempoJogoTextView.text = tempoFormatado
            }

            override fun onFinish() {
                tempoJogoTextView.text = "00:00"
                if(periodo == 4){
                    isGameStarted = false;
                    iniciarJogoButton.text = "Iniciar Novo Jogo"
                }
                if (isGameStarted) {
                    iniciarJogoButton.text = "Iniciar Novo Período"
                }
            }
        }.start() // Inicia o timer
    }

    fun adicionarPontos(pontos: Int, time: String) {
        if(time == "A") {
            pontuacaoTimeA += pontos
        } else {
            pontuacaoTimeB += pontos
        }
        atualizarPlacar(time)
    }

    fun atualizarPlacar(time: String){
        if (time == "A") {
            pTimeATextView.setText(pontuacaoTimeA.toString())
        } else {
            pTimeBTextView.setText(pontuacaoTimeB.toString())
        }
    }

    fun reiniciarPartida() {
        pontuacaoTimeA = 0
        pTimeATextView.setText(pontuacaoTimeA.toString())
        pontuacaoTimeB = 0
        pTimeBTextView.setText(pontuacaoTimeB.toString())
        periodo = 0
        periodoTextView.setText(periodo.toString())
        Toast.makeText(this,"Placar reiniciado",Toast.LENGTH_SHORT).show()
    }

    private fun exibirDialogoDeMetricas() {
        val mensagemDoDialog = "Placar Período 1: ${metricas[0]} x ${metricas[1]}\nPlacar Período 2: ${metricas[2]} x ${metricas[3]}\nPlacar Período 3: ${metricas[4]} x ${metricas[5]}\nPlacar Período 4: ${metricas[6]} x ${metricas[7]}}"

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Métricas do jogo")
        builder.setMessage(mensagemDoDialog)
        builder.setIcon(android.R.drawable.ic_dialog_info)
        builder.setNegativeButton("Fechar", null)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

}