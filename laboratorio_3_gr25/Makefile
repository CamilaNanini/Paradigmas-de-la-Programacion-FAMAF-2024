# Si se desea aumentar la cantidad de workers, debe ejecutarse en la terminal
# export SPARK_WORKER_INSTANCES=$(CANTIDAD_WORKERS)
# donde $(CANTIDAD_WORKERS) debe ser la cantidad de workers que se desea poner al servicio del master

# Configuraciónes para compilar y ejecutar el proyecto
CANT_CORES=1
CANT_MEMORY=1G
# Es obligatorio especificar la ruta de Spark ya que no puede instalarse de forma nativa
SPARK_PATH = /path/to/spark/spark-3.5.1-bin-hadoop3
HOSTNAME=$(shell hostname)
MASTER_ADDRESS=spark://$(HOSTNAME):7077

MVN ?= $(shell which mvn) # Si no esta instalado Maven de forma nativa, borrar esta linea y en la de debajo, especificar la ruta al binario 'mvn'
# MVN = path/to/maven/apache-maven-3.8.4/bin/mvn

# Verifica si la variable MVN está definida y no está vacía
ifeq ($(MVN),)
  $(error Maven no está instalado o la variable MVN está vacía. Por favor, especifique la variable MVN con la ruta de Maven.)
endif

ifndef SPARK_PATH
  $(error "Por favor, especifique la variable SPARK_PATH con la ruta de Spark.")
endif

TARGET_DIR := ./target
JAR_FILE := $(TARGET_DIR)/mi-proyecto-1.0.0.jar
MAIN_CLASS = App

all: run

run: $(JAR_FILE) launch_driver launch_workers
	$(SPARK_PATH)/bin/spark-submit --master $(MASTER_ADDRESS) --class $(MAIN_CLASS) $(JAR_FILE) $(ARGS)

$(JAR_FILE): src/main/java/**/*.java
	@echo "Compilando proyecto"
	@if [ ! -f $(JAR_FILE) ]; then \
        $(MVN) clean package; \
    fi

config:
	echo "SPARK_LOCAL_IP=127.0.1.1" > $(SPARK_PATH)/conf/spark-env.sh

launch_driver: config
# El comando start-master falla si el master ya está corriendo.
	@if ($(SPARK_PATH)/sbin/start-master.sh); then \
		echo "Master is now running"; \
	else \
		echo "WARNING: Master already running"; \
	fi

launch_workers: config
	@if ($(SPARK_PATH)/sbin/start-worker.sh $(MASTER_ADDRESS)  -c $(CANT_CORES) -m $(CANT_MEMORY)); then \
		echo "Workers are now running"; \
	else \
		echo "WARNING: Workers were already running"; \
	fi

stop_workers:
	@echo "WARNING: 	Si la variable SPARK_WORKER_INSTANCES fue editada antes de detener los workers"
	@echo "		no se detendrán todos, y spark perderá la referencia de los workers que estaban corriendo."
	@echo "		En caso que suceda lo segundo ejecute 'pkill -f org.apache.spark.deploy.worker.Worker' manualmente."
	@echo ""
	$(SPARK_PATH)/sbin/stop-worker.sh $(MASTER_ADDRESS)

help:
	@echo "Uso: 			make [all|run|clean|config|launch_driver|launch_workers|stop_workers|help]"
	@echo "all: 			Compila el proyecto y lo ejecuta sin flags."
	@echo "run: 			Ejecuta el proyecto y permite agregar flags."
	@echo "clean: 			Limpia el proyecto."
	@echo "config: 		Configura el archivo spark-env.sh."
	@echo "launch_driver: 		Inicia el master."
	@echo "launch_workers:		Inicia los workers."
	@echo "stop_workers: 		Detiene los workers."
	@echo "help:			Muestra este mensaje."
	@echo ""

	@echo "Opciones del make obligatorias:"
	@echo "MVN:			Ruta de Maven."
	@echo "SPARK_PATH: 		Ruta de Spark."
	@echo ""
	@echo "Opciones del make:"
	@echo "CANT_CORES: 		Cantidad de cores por worker."
	@echo "CANT_MEMORY: 		Cantidad de memoria por worker."
	@echo "NOTA:	Para ejecutar mas de 1 worker, editar la variable SPARK_WORKER_INSTANCES. e.g: (export SPARK_WORKER_INSTANCES=2)."
	@echo "	Se debe tener cuidado al hacer esto, pues cambiar la variable despues de haber iniciado los workers sin haberlos detenido"
	@echo "	puede causar que spark pierda la referencia de los workers que estaban corriendo."
	@echo "	en cualquier caso: \"pkill -f org.apache.spark.deploy.worker.Worker\" detendra los workers incluso si spark los perdió."

	@echo "Ejemplo de uso:"
	@echo "		make run"
	@echo "		make run ARGS=\"-f lmnoti -ne cw -sf cat\""

clean:
	$(SPARK_PATH)/sbin/stop-master.sh
	@ make stop_workers
	$(MVN) clean -q

.PHONY: all run clean
