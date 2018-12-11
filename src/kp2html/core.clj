(ns kp2html.core
  (:gen-class)
  (:require [clojure.data.csv :as csv])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as s]))

(declare item tr)

(defn body [info]
  [(item "username" (:username info))
   (item "password" (:password info))
   (item "url" (:url info))
   (item "notes" (s/replace (:notes info) #"\n" "</br>"))])

(defn entry [e]
  (let [group (first e)
        title (second e)
        info (nthrest e 2)
        keys [:username :password :url :notes]]
    {group [[title (apply hash-map (interleave keys info))]]}))

(defn group [g]
  (let [entries (sort #(compare (first %1) (first %2)) (val g))]
    (conj ["<h1>" (key g) "</h1>"]
          (vec (for [e entries]
                 (conj ["<h1>" (first e) "</h1>"]
                       (body (last e))))))))

(defn html [groups]
  (let [top ["<!doctype html>"
             "<meta charset='utf-8'>"]]
    (apply str (flatten (reduce conj top (map #(group %) groups))))))

(defn item [k v]
  (when (not (s/blank? v)) (str "<p><b>" k "</b></p><p><pre>" v "</pre></p>")))

(defn -main [& args]
  (try
    (with-open [infile (io/reader (first args))]
      (let [csv (rest (csv/read-csv infile))
            groups (reduce #(merge-with concat %1 (entry %2)) {} csv)]
        (println (html groups))))
    (catch Exception e
      (binding [*out* *err*]
        (println (.getMessage e))))))
