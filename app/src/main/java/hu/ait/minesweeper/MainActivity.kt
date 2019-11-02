package hu.ait.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import hu.ait.minesweeper.model.GameMode
import hu.ait.minesweeper.model.MinesweeperBoard
import hu.ait.minesweeper.ui.MinesweeperView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MinesweeperBoard.initializeBoard()

        tbMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                msView.mode = GameMode.FLAG
            } else {
                msView.mode = GameMode.NORMAL
            }
        }

        resetBtn.setOnClickListener {
            msView.resetGame()
        }
    }

    fun updateMineCount(count: Int) {
        tvMine.text = getString(R.string.mines_num, count)
    }

    fun displayWin() {
        makeSnackbar(getString(R.string.won_text))
    }

    fun displayLose() {
        makeSnackbar(getString(R.string.lost_text))
    }

    private fun makeSnackbar(text: String) {
        snackbar = Snackbar.make(linLayout, text, Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ok_text),
            object: View.OnClickListener {
                override fun onClick(v: View?) {
                    snackbar?.dismiss()
                }
            }
        )

        snackbar?.show()
    }
}
