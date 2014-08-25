import google.protobuf.internal.encoder as encoder


class MessageWriter:
  def __init__(self, output):
    self.output = output

  def write(self, message):
    serialized = message.SerializeToString()
    serialized = encoder._VarintBytes(len(serialized)) + serialized
    self.output.write(serialized)
    self.output.flush()
