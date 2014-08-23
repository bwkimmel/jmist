TOPDIR=..
DIR=$(shell pwd)
BUILDDIR=build
PROTO_SRCDIR=ext/build/protobuf-py3
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

$(BUILDDIR)/jmist-blender.zip: protobuf protos srcs jars
	rm -rf $@
	cd $(BUILDDIR) && zip -r jmist-blender.zip jmist-blender

jars: $(BUILDDIR)/jmist-blender/jmist/blender/jmist-pipe.jar

$(JMIST_PIPE_JAR):
	cd $(TOPDIR) && mvn package

$(BUILDDIR)/jmist-blender/jmist/blender/jmist-pipe.jar: $(JMIST_PIPE_JAR)
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

$(PROTO_SRCDIR)/python/build/lib: $(PROTO_SRCDIR)/python/setup.py
	cd $(PROTO_SRCDIR)/python && $(PYTHON3) setup.py build

$(PROTO_SRCDIR)/python/setup.py: $(PROTO_SRCDIR)

protobuf: $(PROTO_SRCDIR)/python/build/lib 
	mkdir -p $(BUILDDIR)/jmist-blender
	cp -r $(PROTO_SRCDIR)/python/build/lib/* $(BUILDDIR)/jmist-blender

srcs: jmist addon.py
	mkdir -p $(BUILDDIR)/jmist-blender
	cp -r jmist $(BUILDDIR)/jmist-blender
	cp -r addon.py $(BUILDDIR)/jmist-blender

clean:
	rm -rf $(BUILDDIR)

distclean: clean
	rm -rf ext/build

ext/build/protobuf-py3: ext/protobuf-py3
	mkdir -p ext/build
	cp -r $< $@

ext/protobuf-py3: $(TOPDIR)/.gitmodules
	git submodule update $@

$(PROTO_SRCDIR)/autogen.sh: $(PROTO_SRCDIR)

$(PROTO_SRCDIR)/configure: $(PROTO_SRCDIR)/autogen.sh
	cd $(PROTO_SRCDIR) && ./autogen.sh

$(PROTO_SRCDIR)/Makefile: $(PROTO_SRCDIR)/configure
	cd $(PROTO_SRCDIR) && ./configure --prefix=$(DIR)/$(PROTO_HOME)

$(PROTOC): $(PROTO_SRCDIR)/Makefile
	cd $(PROTO_SRCDIR) && make install

