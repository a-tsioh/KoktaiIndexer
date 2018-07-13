package models

object ApiService extends api.SharedApi {
  override def test(): api.Chapter = api.Chapter("coucou",Nil,Nil,"comment")
}
