package com.clemaire.cache.impl.io.writing

import com.clemaire.cache.definitions.Identifiable
import com.clemaire.cache.definitions.io.writing.DataWriter

abstract class BasicDataWriter[D <: Identifiable](val length: Int)
  extends DataWriter[D]
