/*
 * Copyright (c) VMware, Inc. 2022. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

CREATE TABLE IF NOT EXISTS customers (
	id BIGINT PRIMARY KEY,
  	name VARCHAR(256) NOT NULL
);
