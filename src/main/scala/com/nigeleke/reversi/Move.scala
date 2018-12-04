package com.nigeleke.reversi

case class Move(row: Int, col: Int)

//object Move {
//
//  implicit def liftImplicitTuple2[A, B, A1, B1](tuple: (A, B))(implicit f1: A => A1, f2: B => B1) : (A1, B1) =
//    (f1(tuple._1), f2(tuple._2))
//
//}
