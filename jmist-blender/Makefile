TOPDIR=..
BUILDDIR=build
PROTOC=protoc
PYTHON3=python3
VERSION=$(shell xmlstarlet sel -t -v '/_:project/_:version/text()' $(TOPDIR)/pom.xml)
JMIST_PIPE_JAR=$(TOPDIR)/jmist-pipe/target/jmist-pipe-$(VERSION).jar

JMIST_PROTO_PATH=$(TOPDIR)/jmist-proto/src/main/proto
JMIST_PIPE_PROTO_PATH=$(TOPDIR)/jmist-pipe/src/main/proto

all: dist

dist: $(BUILDDIR)/jmist-blender.zip

$(BUILDDIR)/jmist-blender.zip: protos srcs jars deps
	rm -f $@
	cd $(BUILDDIR)/jmist-blender && zip -rq ../jmist-blender.zip *

jars: $(BUILDDIR)/jmist-blender/jmist/jmist-pipe.jar

$(BUILDDIR)/jmist-blender/jmist/jmist-pipe.jar: $(JMIST_PIPE_JAR)
	cp $< $@

protos: $(BUILDDIR)/proto
	mkdir -p $(BUILDDIR)/jmist-blender
	$(PROTOC) --python_out=$(BUILDDIR)/jmist-blender \
	          --proto_path=$(BUILDDIR)/proto \
	          `find $(BUILDDIR)/proto -name '*.proto'`

$(BUILDDIR)/proto:
	rm -rf $(BUILDDIR)/proto
	mkdir -p $(BUILDDIR)/proto
	cp -r $(JMIST_PROTO_PATH)/* $(BUILDDIR)/proto
	cp -r $(JMIST_PIPE_PROTO_PATH)/* $(BUILDDIR)/proto

#pkg_resources: $(BUILDDIR)/jmist-blender/pkg_resources.py

deps: $(BUILDDIR)/deps
	mkdir -p $(BUILDDIR)/jmist-blender
	cp -r $</* $(BUILDDIR)/jmist-blender

$(BUILDDIR)/deps: requirements.txt
	virtualenv -p $(PYTHON3) $(BUILDDIR)/env
	. $(BUILDDIR)/env/bin/activate && pip3 install -r requirements.txt --target $(BUILDDIR)/deps && deactivate

srcs: jmist
	mkdir -p $(BUILDDIR)/jmist-blender
	cp -r jmist $(BUILDDIR)/jmist-blender

clean:
	rm -rf $(BUILDDIR)
