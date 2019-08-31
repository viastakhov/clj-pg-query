(ns clj-pg-query.utils
  (:require [clj-time.core :as tc]
            [clj-time.format :as tf]
            [clj-time.local :as tl])
  (:gen-class))

(defn string-to-date
  "Sampe as java class SimpleDateFormat, i.e.:
        String dateStr = \"14-08-2016\";
        DateFormat formatter = new SimpleDateFormat(\"dd-MM-yyyy\");
        Date date = (Date)formatter.parse(dateStr);"
  [format date-str]
  (let [formatter (java.text.SimpleDateFormat. format)]
    (.parse formatter date-str)))

(defn date-to-string
  [format date]
  (let [formatter (java.text.SimpleDateFormat. format)]
    (.format formatter date)))

(defn random
 "Generate random int from 'from' to 'to'"
 [from to]
 (+ from (rand-int (- to from))))

(defmacro trace []
 "Returns a string, the current Clojure class/function and line"
 `(str (-> (Thread/currentThread) .getStackTrace second .getClassName)
       ":"
       (-> (Thread/currentThread) .getStackTrace second .getLineNumber)))

(defn uuid []
  (.toString (java.util.UUID/randomUUID)))

(defn random
 "Generate random int from 'from' to 'to'"
 [from to]
 (+ from (rand-int (- to from))))

(defn get-local-time
  "Get current local time on service"
  []
  (tc/from-time-zone (tl/local-now) (tc/time-zone-for-offset 0)))

(defn arg-count
  "Get count of args in the function f"
  [f]
  {:pre [(instance? clojure.lang.AFunction f)]}
  (-> f class .getDeclaredMethods first .getParameterTypes alength))
