import javax.inject.Inject

import play.api.http.DefaultHttpFilters
import play.filters.cors.CORSFilter
import play.filters.csrf.CSRFFilter
import play.filters.headers.SecurityHeadersFilter
import play.filters.hosts.AllowedHostsFilter

/**
  * Add the following filters by default to all projects
  *
  * https://www.playframework.com/documentation/latest/ScalaCsrf
  * https://www.playframework.com/documentation/latest/AllowedHostsFilter
  * https://www.playframework.com/documentation/latest/SecurityHeaders
  */
class Filters @Inject()(corsFilter: CORSFilter,
                        allowedHostsFilter: AllowedHostsFilter,
                        securityHeadersFilter: SecurityHeadersFilter)
    extends DefaultHttpFilters(
        corsFilter,
        allowedHostsFilter,
        securityHeadersFilter)