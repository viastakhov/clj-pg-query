(require 'cemerick.pomegranate.aether)

(cemerick.pomegranate.aether/register-wagon-factory! "http" #(org.apache.maven.wagon.providers.http.HttpWagon.))

(defproject clj-pg-query "1.1.0-SNAPSHOT"
  :description "A microservice for talking to PostgreSQL instances"
  :url "https://github.com/viastakhov/clj-pg-query"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [metosin/compojure-api "1.1.11"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [stylefruits/gniazdo-jsr356 "1.0.0"]
                 [javax.servlet/javax.servlet-api "4.0.1"]
                 [clj-http "3.9.1"]
                 [clj-time "0.15.0"]
                 [cheshire "5.8.0"]
                 [environ "1.1.0"]
                 [org.taoclj/foundation "0.1.3" :exclusions [com.impossibl.pgjdbc-ng/pgjdbc-ng]]
                 [com.impossibl.pgjdbc-ng/pgjdbc-ng "0.7.1"]]
  :ring {:handler clj-pg-query.handler/app
         :port 8080}
  :uberjar-name "server.jar"
  :repositories [["repo1" {:url "http://repo1.maven.org/maven2"
                           :snapshots true}]
                 ["clojars" {:url "http://repo.clojars.org/"
                             :snapshots true}]]
  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "4.0.1"]
                                  [proto-repl "0.3.1"]]
                   :plugins [[lein-ring "0.12.5"]
                             [lein-environ "1.0.3"]]
                   :env {:api-key nil}}
             :uberjar {:aot :all}})
