package hu.ait.minesweeper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import hu.ait.minesweeper.model.MinesweeperBoard
import kotlinx.android.synthetic.main.activity_creator.*

class CreatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)

        btnSubmit.setOnClickListener {
            when {
                etDimension.text.isEmpty() -> etDimension.error = getString(R.string.error_dimension)
                etMines.text.isEmpty() -> etMines.error = getString(R.string.error_mines)
                else -> validateCreatorActivity()
            }
        }
    }

    private fun validateCreatorActivity() {
        var dim = etDimension.text.toString().toInt()
        var mines = etMines.text.toString().toInt()

        when {
            dim < 3 || dim > 12 -> etDimension.error = getString(R.string.error_dimension_input)
            mines > dim * dim / 2 -> etMines.error = getString(R.string.error_mines_input)
            else -> startCreatedActivity(dim, mines)
        }
    }

    private fun startCreatedActivity(dim: Int, mines: Int) {
        setValues(dim, mines)

        startActivity(
            Intent(
                this@CreatorActivity,
                MainActivity::class.java
            )
        )
    }

    private fun setValues(dim: Int, mines: Int) {
        MinesweeperBoard.DIMENSION = dim
        MinesweeperBoard.MINES_NUM = mines
    }
}
