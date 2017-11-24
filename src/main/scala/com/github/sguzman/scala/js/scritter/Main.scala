package com.github.sguzman.scala.js.scritter

import org.scalajs.dom
import dom.document

object Main {
  def main(args: Array[String]): Unit = {
    appendPar(document.body, "Hello world")
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }
}
