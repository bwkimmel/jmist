import google.protobuf.internal.decoder as decoder


class MessageReader:
  def __init__(self, message_factory, input):
    self.message_factory = message_factory
    self.input = input
    self.buffer = bytes()
    self.min_buffer_size = 10  # This must be enough to hold a varint.
  
  def _requireBytes(self, total_bytes):
    bytes_needed = total_bytes - len(self.buffer)
    while bytes_needed > 0:
      bytes_in = self.input.read(bytes_needed)
      if len(bytes_in) == 0:
        return False
      bytes_needed = bytes_needed - len(bytes_in)
      self.buffer = self.buffer + bytes_in
    return True
    
  def read(self):
    if not self._requireBytes(self.min_buffer_size) and self.buffer == 0:
      return None
    (size, position) = decoder._DecodeVarint(self.buffer, 0)

    if not self._requireBytes(position + size):
      raise Exception('Unexpected end of file.')
    message = self.message_factory()
    message.ParseFromString(self.buffer[position:position+size])
    
    # It is possible that in reading enough bytes to parse the message length,
    # we may have read all the way into the next message.  So make sure we
    # preserve those leading bytes of the next message, if any.
    self.buffer = self.buffer[position+size:]
      
    return message
