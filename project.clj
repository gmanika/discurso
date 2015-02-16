(defproject discurso "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clojure-opennlp "0.3.3"]
                 [cheshire "5.4.0"]
                 [com.taoensso/nippy "2.7.0"]
                 [clojure-msgpack "0.1.0-SNAPSHOT"]
                 [com.google.guava/guava "18.0"]
                 [intervox/clj-progress "0.1.6"]
                 [ring "1.3.2"]
                 [compojure "1.3.1"]
                 [javax.servlet/servlet-api "2.5"]]
  :main discurso.core
  :plugins [[lein-ring "0.9.1"]
            [com.palletops/uberimage "0.4.1"]]
  :ring {:handler discurso.handler/app}
  :aot :all
  :jvm-opts ["-Xmx8g" "-server" "-XX:+UseCompressedOops"] )
