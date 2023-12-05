package com.lassis.sonic.command;

import jakarta.annotation.Nonnull;

public record Partition(@Nonnull String collection, @Nonnull String bucket) {}
