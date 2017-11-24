package com.github.sguzman.scala.js.scritter

import google.maps.LatLng
import org.scalajs.dom
import org.scalajs.dom.document

import scala.scalajs.js

object Main {
  def main(args: Array[String]): Unit = {
    def initialize() = js.Function {
      val opts = google.maps.MapOptions(
        center = new LatLng(51.201203, -1.724370),
        zoom = 8,
        panControl = false,
        streetViewControl = false,
        mapTypeControl = false
      )
      val gmap = new google.maps.Map(document.getElementById("map"), opts)
      ""
    }
    google.maps.event.addDomListener(org.scalajs.dom.window, "load", initialize())
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }
}
