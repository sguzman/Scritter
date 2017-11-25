package com.github.sguzman.scala.js.scritter

import google.maps.LatLng
import org.scalajs.dom.document

import scala.scalajs.js

import fr.hmil.roshttp.HttpRequest
import monix.execution.Scheduler.Implicits.global
import scala.util.{Failure, Success}
import fr.hmil.roshttp.response.SimpleHttpResponse

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

object Main {
  var map: Option[google.maps.Map] = None
  case class Tweet(created_at: String, id: Long, text: String)
  case class Tweets(tweets: Array[Tweet])
  var tweets: Tweets = _
  def main(args: Array[String]): Unit = {
    google.maps.event.addDomListener(org.scalajs.dom.window, "load", initialize())

    val request = HttpRequest("https://tranquil-island-19340.herokuapp.com/")
    request.send.onComplete({
      case res:Success[SimpleHttpResponse] =>
        this.tweets = decode[Tweets](res.get.body).getOrElse(Tweets(Array[Tweet]()))
        val latLngs = this.tweets.tweets.map(_.text).filter(_.matches("^.*\\*34[0-9]+ -1[0-9]+ \\*.*$")).map(s => {
          val Pattern = "^.*\\*(34[0-9]+) (-1[0-9]+) \\*.*$".r
          val Pattern(latStr, lngStr) = s
          val (preLat, postLat) = latStr.splitAt(2)
          val (preLng, postLng) = lngStr.splitAt(4)
          val lat = preLat +  "." + postLat
          val lng = preLng + "." + postLng
          (lat.toDouble, lng.toDouble)
        }) map (t => new google.maps.Marker(google.maps.MarkerOptions(
          position = new google.maps.LatLng(t._1, t._2, false),
          map = this.map.get,
          title = t.toString
        )))

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
    this.map = Some(new google.maps.Map(document.getElementById("map"), opts))
    ""
  }
}
