# To support reload properly, try to access a package var,
# if it's there, reload everything
if "bpy" in locals():
    import imp
    imp.reload(engine)
    imp.reload(mesh_exporter)
    imp.reload(message_reader)
    imp.reload(message_writer)
else:
    from . import engine, mesh_exporter, message_reader, message_writer

import bpy
