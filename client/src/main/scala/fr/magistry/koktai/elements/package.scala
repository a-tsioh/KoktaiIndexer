package fr.magistry.koktai

package object elements {
  abstract class Elem

  case class Chapter(name: String) extends Elem
  case class Sinogram(hanji: String) extends Elem
  case class Word(w: String) extends Elem

}
