package hu.ait.minesweeper.model

data class Field (var type: FieldType, var minesAround: Int, var isFlagged: Boolean, var wasTouched: Boolean)