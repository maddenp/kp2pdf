(defproject kp2html "0.0.1"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.3"]]
  :uberjar-name "kp2html.jar"
  :main ^:skip-aot kp2html.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
