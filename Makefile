TOP_DIR = ../..
TMP_DIR = /tmp
include $(TOP_DIR)/tools/Makefile.common
KB_RUNTIME = /kb/runtime
DEPLOY_RUNTIME = $(KB_RUNTIME)
KB_TOP = /kb/deployment
TARGET = $(KB_TOP)
CURR_DIR = $(shell pwd)
TARGET_DIR = $(TARGET)/services/$(SERVICE_NAME)
TARGET_PORT = 7078
THREADPOOL_SIZE = 20
SERVICE_NAME = $(shell basename $(CURR_DIR))
SERVICE_SPEC = ./MAK.spec
SERVICE_PORT = $(TARGET_PORT)
SERVICE_DIR = $(TARGET_DIR)
SERVLET_CLASS = us.kbase.mak.MAKServer
MAIN_CLASS = us.kbase.mak.MAKInvoker
SERVICE_PSGI = $(SERVICE_NAME).psgi
TPAGE_ARGS = --define kb_top=$(TARGET) --define kb_runtime=$(DEPLOY_RUNTIME) --define kb_service_name=$(SERVICE_NAME) --define kb_service_dir=$(SERVICE_DIR) --define kb_service_port=$(SERVICE_PORT) --define kb_psgi=$(SERVICE_PSGI)
SCRIPTS_TESTS = $(wildcard script-tests/*.t)
DEPLOY_JAR = $(KB_TOP)/lib/jars/mak
DATA_DIR = /var/tmp/mak/data
SCRIPTS_TESTS_CLUSTER = $(wildcard script-test-cluster/*.t)
GLASSFISH_HOME ?= $(DEPLOY_RUNTIME)/glassfish3

default: compile

# fake deploy-cfg target for when this is run outside the dev_container
deploy-cfg:

ifeq ($(TOP_DIR_NAME), dev_container)
include $(TOP_DIR)/tools/Makefile.common.rules
endif

deploy: deploy-client deploy-scripts deploy-service deploy-jar deploy-cfg

deploy-all: deploy-client

deploy-jar: deploy-sh-scripts distrib-jar test-jar

compile-jar: src lib
	./make_jar.sh $(MAIN_CLASS)

distrib-jar:
	export KB_TOP=$(TARGET)
	rm -rf $(DEPLOY_JAR)
	mkdir -p $(DEPLOY_JAR)/lib
	cp ./lib/*.jar $(DEPLOY_JAR)/lib
	cp ./lib//MAK.jar $(DEPLOY_JAR)


deploy-client: deploy-libs deploy-pl-scripts deploy-docs

deploy-libs: build-libs
	rsync --exclude '*.bak*' -arv lib/. $(TARGET)/lib/.

deploy-pl-scripts:
	echo "deploy-pl-scripts"
	export KB_TOP=$(TARGET); \
	export KB_RUNTIME=$(DEPLOY_RUNTIME); \
	export KB_PERL_PATH=$(TARGET)/lib bash ; \
	for src in $(SRC_PERL) ; do \
		basefile=`basename $$src`; \
		base=`basename $$src .pl`; \
		echo install $$src $$base ; \
		cp $$src $(TARGET)/plbin ; \
		$(WRAP_PERL_SCRIPT) "$(TARGET)/plbin/$$basefile" $(TARGET)/bin/$$base ; \
	done

deploy-docs: build-docs
	mkdir -p $(TARGET)/services/$(SERVICE_NAME)/webroot/.
	cp docs/*.html $(TARGET)/services/$(SERVICE_NAME)/webroot/.


SRC_SH = $(wildcard scripts/*.sh)
WRAP_SH_TOOL = wrap_sh
WRAP_SH_SCRIPT = bash $(TOOLS_DIR)/$(WRAP_SH_TOOL).sh

deploy-sh-scripts:
	mkdir -p $(TARGET)/shbin; \
	export KB_TOP=$(TARGET); \
	export KB_RUNTIME=$(DEPLOY_RUNTIME); \
	for src in $(SRC_SH) ; do \
		basefile=`basename $$src`; \
		base=`basename $$src .sh`; \
		echo install $$src $$base ; \
		cp $$src $(TARGET)/shbin ; \
		$(WRAP_SH_SCRIPT) "$(TARGET)/shbin/$$basefile" $(TARGET)/bin/$$base ; \
	done 

deploy-service:
	@echo "Target folder: $(TARGET_DIR)"
	mkdir -p $(TARGET_DIR)
	mkdir -p $(TMP_DIR)
	cp deploy.cfg $(TARGET_DIR)
	cp -f ./dist/MAK.war $(TARGET_DIR)
	cp -f ./start_service $(TARGET_DIR)
	cp -f ./stop_service $(TARGET_DIR)
	cp -f ./MAK.awf $(TARGET_DIR)
	#echo "MAK=$(DEPLOY_RUNTIME)/MAK-python/\nujs_url=$(UJS_SERVICE_URL)\nawe_url=$(AWE_CLIENT_URL)\nid_url=$(ID_SERVICE_URL)\nws_url=$(WS_SERVICE_URL)\nawf_config=$(TARGET_DIR)/MAK.awf" > $(TARGET_DIR)/MAK.properties
	#echo "./glassfish_start_service.sh $(TARGET_DIR)/service.war $(TARGET_PORT) $(THREADPOOL_SIZE)" > $(TARGET_DIR)/start_service.sh
	chmod +x $(TARGET_DIR)/start_service
	#echo "./glassfish_stop_service.sh $(TARGET_PORT)" > $(TARGET_DIR)/stop_service.sh
	chmod +x $(TARGET_DIR)/stop_service

build-docs: compile-docs
	pod2html --infile=lib/Bio/KBase/$(SERVICE_NAME)/Client.pm --outfile=docs/$(SERVICE_NAME).html

compile-docs: build-libs

build-libs:
	compile_typespec \
		--psgi $(SERVICE_PSGI)  \
		--impl Bio::KBase::$(SERVICE_NAME)::$(SERVICE_NAME)Impl \
		--service Bio::KBase::$(SERVICE_NAME)::Service \
		--client Bio::KBase::$(SERVICE_NAME)::Client \
		--py biokbase/$(SERVICE_NAME)/Client \
		--js javascript/$(SERVICE_NAME)/Client \
		$(SERVICE_SPEC) lib

compile: src lib
	chmod +x ./make_war.sh
	./make_war.sh $(SERVLET_CLASS)

deploy: compile
	cd $(TARGET)
	curl -O http://genomics.lbl.gov/~marcin/MAK_data.tar.gz
	tar zxf MAK_data.tar.gz


test: test-scripts
	@echo "running script tests"

test-scripts:
	# run each test
	for t in $(SCRIPTS_TESTS) ; do \
		if [ -f $$t ] ; then \
			$(DEPLOY_RUNTIME)/bin/perl $$t ; \
			if [ $$? -ne 0 ] ; then \
				exit 1 ; \
			fi \
		fi \
	done

test-jar:
	@echo "nothing to test"

clean:
	@echo "nothing to clean"
	
include $(TOP_DIR)/tools/Makefile.common.rules	
