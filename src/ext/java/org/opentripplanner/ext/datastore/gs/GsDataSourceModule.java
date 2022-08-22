package org.opentripplanner.ext.datastore.gs;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Nullable;
import javax.inject.Singleton;
import org.opentripplanner.datastore.api.GoogleStorageDSRepository;
import org.opentripplanner.datastore.base.DataSourceRepository;
import org.opentripplanner.standalone.config.BuildConfig;
import org.opentripplanner.util.OTPFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Module
public class GsDataSourceModule {

  private static final Logger LOG = LoggerFactory.getLogger(GsDataSourceModule.class);

  @Provides
  @Singleton
  @Nullable
  @GoogleStorageDSRepository
  DataSourceRepository provideGoogleStorageDataSourceRepository(BuildConfig config) {
    if (OTPFeature.GoogleCloudStorage.isOff()) {
      return null;
    }
    LOG.info("Google Cloud Store Repository enabled - GS resources detected.");
    return new GsDataSourceRepository(config.gsCredentials);
  }
}
