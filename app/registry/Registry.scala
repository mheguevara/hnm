package registry

import components.hnservice.{HNServiceRSSImplComponent, HNServiceComponent}
import components.archive.{ArchiveCacheImplComponent, ArchiveComponent}


/**
 * Copyright 2003-2014 Monitise Group Limited. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Monitise Group Limited.
 * Any reproduction of this material must contain this notice.
 */
trait Registry extends HNServiceComponent with ArchiveComponent

trait ProductionEnvironment extends Registry with HNServiceRSSImplComponent with ArchiveCacheImplComponent