(ns clj-pg-query.schemas
  (:require [schema.core :as s]
            [compojure.api.sweet :as sw])
  (:gen-class))


(s/defschema DatasourceConfig
  {:host     String
   :port     Long
   :database String
   :username String
   :password String})

(s/defschema Where
  {s/Any s/Any})

(s/defschema Data
  {s/Any s/Any})

(s/defschema Clause
  {:columns {s/Any s/Any}
   :where {s/Any s/Any}})
