package com.edmundwalsh.model

case class PageRank(url: String, depth: Int, ratio: Double) {
  def toTSV = {
    url + "\t" + depth.toString + "\t" + ratio.toString
  }
}
