package hu.ait.minesweeper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import hu.ait.minesweeper.model.MinesweeperBoard
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        beginnerBtnListener()
        masterBtnListener()
        creatorBtnListener()
    }

    fun beginnerBtnListener() {
        beginnerBtn.setOnClickListener {
            setValues(5, 3)

            startActivity(
                Intent(
                    this@HomeActivity,
                    MainActivity::class.java
                )
            )
        }
    }

    fun masterBtnListener() {
        masterBtn.setOnClickListener {
            setValues(12, 40)

            startActivity(
                Intent(
                    this@HomeActivity,
                    MainActivity::class.java
                )
            )
        }
    }

    fun creatorBtnListener() {
        creatorBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    CreatorActivity::class.java
                )
            )

        }
    }

    private fun setValues(dim: Int, mines: Int) {
        MinesweeperBoard.DIMENSION = dim
        MinesweeperBoard.MINES_NUM = mines
    }

}
