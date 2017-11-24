package com.github.sguzman.scala.js.scritter

import google.maps.LatLng
import org.scalajs.dom.document

import scala.scalajs.js

import fr.hmil.roshttp.HttpRequest
import monix.execution.Scheduler.Implicits.global
import scala.util.{Failure, Success}
import fr.hmil.roshttp.response.SimpleHttpResponse

object Main {
  def main(args: Array[String]): Unit = {
    google.maps.event.addDomListener(org.scalajs.dom.window, "load", initialize())

    val request = HttpRequest("https://tranquil-island-19340.herokuapp.com/")
    request.send().onComplete({
      case res:Success[SimpleHttpResponse] => append(document.body, res.get.body)
      case e: Failure[SimpleHttpResponse] => println("Huston, we got a problem!")
    })
  }

  def append[A](node: org.scalajs.dom.Node, text: A): Unit = {
    val newNode = document.createElement("p")
    val textNode = document.createTextNode(text.toString)
    newNode.appendChild(textNode)
    node.appendChild(newNode)
  }

  def initialize() = js.Function {
    val opts = google.maps.MapOptions(
      center = new LatLng(34.412593, -119.861087),
      zoom = 15,
      panControl = false,
      streetViewControl = false,
      mapTypeControl = false
    )
    val gmap = new google.maps.Map(document.getElementById("map"), opts)
    ""
  }
}
