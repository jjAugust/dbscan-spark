package com.esri.dbscan

import scala.collection.mutable.ArrayBuffer

/**
  * Location in 2D space.
  *
  * @param id the point identifier.
  * @param x  the horizontal 2D placement.
  * @param y  the vertical 2D placement.
  */
class Point(val id: Int, val x: Double, val y: Double) extends Serializable {

  /**
    * Check if supplied point is the same as this point. This is a naive implementation as it checks only the point identifier.
    *
    * @param other the other point.
    * @return if the other point id is the same as this point id.
    */
  override def equals(other: Any): Boolean = other match {
    case that: Point => id == that.id
    case _ => false
  }

  /**
    * Hash representation of this point
    *
    * @return the hash of the point id.
    */
  override def hashCode(): Int = {
    Smear.smear(id)
  }

  /*
    def distance2(that: Point) = {
      val dx = x - that.x
      val dy = y - that.y
      dx * dx + dy * dy
    }
  */

  /**
    * Convert this point to a sequence of <code>Cell</code> instances based on its location in its parent cell.
    * The parent cell is the cell whose envelope wholly contains this point.
    * If the point is within <code>eps</code> distance of the edge of the cell, the neighboring cell is added to the aforementioned sequence.
    *
    * @param cellSize the parent cell size
    * @param eps      the neighborhood distance.
    * @return the parent cell and all the neighboring cells if the point is close to the edge.
    */
  def toCells(cellSize: Double, eps: Double) = {
    val xfac = (x / cellSize).floor
    val yfac = (y / cellSize).floor
    val cx = xfac * cellSize
    val cy = yfac * cellSize
    val xmin = cx + eps
    val ymin = cy + eps
    val xmax = cx + cellSize - eps
    val ymax = cy + cellSize - eps
    val row = yfac.toInt
    val col = xfac.toInt
    val cellArr = new ArrayBuffer[Cell](4)
    cellArr += Cell(row, col)
    if (x < xmin) {
      cellArr += Cell(row, col - 1)
      if (y < ymin) {
        cellArr += Cell(row - 1, col - 1)
        cellArr += Cell(row - 1, col)
      } else if (y > ymax) {
        cellArr += Cell(row + 1, col - 1)
        cellArr += Cell(row + 1, col)
      }
    } else if (x > xmax) {
      cellArr += Cell(row, col + 1)
      if (y < ymin) {
        cellArr += Cell(row - 1, col + 1)
        cellArr += Cell(row - 1, col)
      } else if (y > ymax) {
        cellArr += Cell(row + 1, col + 1)
        cellArr += Cell(row + 1, col)
      }
    } else if (y < ymin) {
      cellArr += Cell(row - 1, col)
    } else if (y > ymax) {
      cellArr += Cell(row + 1, col)
    }
    cellArr
  }

  /**
    * @return text representation of this instance.
    */
  override def toString = s"Point($id,$x,$y)"

}

/**
  * Companion object to build a point
  */
object Point extends Serializable {
  /**
    * Instantiate a point given an id, x and y value.
    *
    * @param id the point identifier
    * @param x  the horizontal placement
    * @param y  the vertical placement
    * @return a new <code>Point</code> instance.
    */
  def apply(id: Int, x: Double, y: Double) = {
    new Point(id, x, y)
  }
}
