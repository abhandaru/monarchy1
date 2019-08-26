package monarchy

import sangria.schema.Schema

package object graphql {
  val GraphqlSchema = Schema(QuerySchema.Def, Some(MutationSchema.Def))
}
