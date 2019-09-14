(ns clj-pg-query.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [environ.core :refer [env]]
            [clj-pg-query.utils :as utils]
            [clj-pg-query.schemas :as schemas]
            [clj-pg-query.core :as core]))


(defn authorized? [handler]
   (fn [request]
     (let [auth-header (get (:headers request) "authorization")
           api-key (env :api-key)]
       (if (or (not (seq api-key)) (= auth-header api-key))
           (handler request)
           (unauthorized "Please authorize first!")))))


(def app
  (api
    {:default-format "application/json"
     :swagger
      {:ui "/"
       :spec "/swagger.json"
       :data {:info {:title "clj-pg-query"
                     :description "A microservice for talking to PostgreSQL instances"
                     :contact {:name "Vladimir Astakhov"
                               :email "viastakhov@mail.ru"
                               :url "https://github.com/viastakhov/clj-pg-query"}
                     :version "1.1.0"
                     :license {:name "Eclipse Public License"
                               :url "http://www.eclipse.org/legal/epl-v10.html"}}
              :consumes ["application/json"]
              :produces ["application/json"]
              :securityDefinitions {:api_key {:type "apiKey" :name "Authorization" :in "header"}}
              :tags [{:name "datasource", :description "JDBC datasource"}
                     {:name "statement", :description "Executes statements"}]}}}


    (context "/v1" []
      :middleware [authorized?]
      :tags ["datasource"]
      (context "/datasource" []

        (POST "/define" []
          :body [config schemas/DatasourceConfig]
          :summary "Define a JDBC datasource"
          (core/set-datasource-config config)
          (ok)))


      (context "/statement" []
        :tags ["statement"]

        (POST "/execute" []
          :header-params  [query :- String]
          :summary "Execute raw SQL command"
          (let [result (core/execute-raw-query query)]
            (if (nil? result)
              (not-acceptable "Please define a datasource connection")
              (ok result))))

        (POST "/select" []
          :return [{s/Any s/Any}]
          :query-params [Table :- s/Keyword]
          :body [Where schemas/Where]
          :summary "Execute DSL SELECT statement"
          (let [result (core/execute-select Table Where)]
            (if (nil? result)
              (not-acceptable "Please define a datasource connection")
              (ok result))))

        (POST "/insert" []
          :return String
          :query-params [Table :- s/Keyword]
          :body [Data schemas/Data]
          :summary "Execute DSL INSERT statement to insert single record"
          (let [result (core/execute-insert Table Data)]
            (if (nil? result)
              (not-acceptable "Please define a datasource connection")
              (ok result))))

        (PUT "/update" []
          :query-params [Table :- s/Keyword]
          :body [Clause schemas/Clause]
          :summary "Execute DSL UPDATE statement"
          (let [result (core/execute-update Table Clause)]
            (if (nil? result)
              (not-acceptable "Please define a datasource connection")
              (ok))))

        (DELETE "/delete" []
          :query-params [Table :- s/Keyword]
          :body [Where schemas/Where]
          :summary "Execute DSL DELETE statement"
          (let [result (core/execute-delete Table Where)]
            (if (nil? result)
              (not-acceptable "Please define a datasource connection")
              (ok))))))))
