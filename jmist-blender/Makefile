TOPDIR=..
DIR=$(shell pwd)
BUILDDIR=build
PROTO_SRCDIR=ext/build/protobuf
PROTO_HOME=build/protobuf
PROTOC=$(PROTO_HOME)/bin/protoc
PYTHON3=python3
VERSION=$(shell xmlstarlet sel -t -v '/_:project/_:version/text()' ../pom.xml)
JMIST_PIPE_JAR=$(TOPDIR)/jmist-pipe/target/jmist-pipe-$(VERSION).jar

JMIST_PROTOS=$(TOPDIR)/jmist-proto/src/main/proto/*.proto
JMIST_PIPE_PROTOS=$(TOPDIR)/jmist-pipe/src/main/proto/*.proto
ALL_PROTOS=$(JMIST_PROTOS) $(JMIST_PIPE_PROTOS)

all: dist

dist: $(BUILDDIR)/jmist-blender.zip

$(BUILDDIR)/jmist-blender.zip: protobuf protos srcs jars pkg_resources
	rm -rf $@
	cd $(BUILDDIR)/jmist-blender && zip -r ../jmist-blender.zip *

jars: $(BUILDDIR)/jmist-blender/jmist/jmist-pipe.jar

$(JMIST_PIPE_JAR):
	cd $(TOPDIR) && mvn package

$(BUILDDIR)/jmist-blender/jmist/jmist-pipe.jar: $(JMIST_PIPE_JAR)
	cp $< $@

protos: $(PROTOC) $(BUILDDIR)/proto
	mkdir -p $(BUILDDIR)/jmist-blender/jmist/proto
	$(PROTOC) --python_out=$(BUILDDIR)/jmist-blender/jmist/proto \
					  --proto_path=$(BUILDDIR)/proto \
						$(BUILDDIR)/proto/*.proto

$(BUILDDIR)/proto:
	rm -rf $(BUILDDIR)/proto
	mkdir -p $(BUILDDIR)/proto
	cp $(JMIST_PROTOS) $(BUILDDIR)/proto
	cp $(JMIST_PIPE_PROTOS) $(BUILDDIR)/proto

pkg_resources: $(BUILDDIR)/jmist-blender/pkg_resources.py

$(BUILDDIR)/jmist-blender/pkg_resources.py:
	virtualenv -p $(PYTHON3) $(BUILDDIR)/env
	cp $(BUILDDIR)/env/lib/python*/site-packages/pkg_resources.py $(BUILDDIR)/jmist-blender

$(PROTO_SRCDIR)/python/build/lib: $(PROTO_SRCDIR)/python/setup.py $(PROTOC)
	cd $(PROTO_SRCDIR)/python && $(PYTHON3) setup.py build

$(PROTO_SRCDIR)/python/setup.py: $(PROTO_SRCDIR)

protobuf: $(PROTO_SRCDIR)/python/build/lib 
	mkdir -p $(BUILDDIR)/jmist-blender
	cp -r $(PROTO_SRCDIR)/python/build/lib/* $(BUILDDIR)/jmist-blender

srcs: jmist
	mkdir -p $(BUILDDIR)/jmist-blender
	cp -r jmist $(BUILDDIR)/jmist-blender

clean:
	rm -rf $(BUILDDIR)

distclean: clean
	rm -rf ext/build

ext/build/protobuf: ext/protobuf
	mkdir -p ext/build
	cp -r $< $@

ext/protobuf: ext/protobuf-python-3.1.0.tar.gz
	mkdir -p $@
	tar xzvf $< -C $@ --strip-components 1

ext/protobuf-python-3.1.0.tar.gz:
	curl -L 'https://github.com/google/protobuf/releases/download/v3.1.0/protobuf-python-3.1.0.tar.gz' -o $@

$(PROTO_SRCDIR)/Makefile: $(PROTO_SRCDIR)/configure
	cd $(PROTO_SRCDIR) && ./configure --prefix=$(DIR)/$(PROTO_HOME)

$(PROTOC): $(PROTO_SRCDIR)/Makefile
	cd $(PROTO_SRCDIR) && make && make install

