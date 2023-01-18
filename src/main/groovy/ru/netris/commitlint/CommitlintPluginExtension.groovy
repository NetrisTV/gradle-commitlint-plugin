package ru.netris.commitlint

import org.gradle.api.provider.Property

interface CommitlintPluginExtension {
  Property<Boolean> getEnforceRefs()
}