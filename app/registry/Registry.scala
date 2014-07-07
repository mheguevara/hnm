package registry

import components.hnservice.{HNServiceRSSImplComponent, HNServiceComponent}
import components.archive.{ArchiveCacheImplComponent, ArchiveComponent}

trait Registry extends HNServiceComponent with ArchiveComponent

trait ProductionEnvironment extends Registry with HNServiceRSSImplComponent with ArchiveCacheImplComponent