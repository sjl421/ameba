package ameba.db.model;

import ameba.db.DataSourceFeature;
import ameba.exception.AmebaException;
import ameba.feature.AmebaFeature;
import ameba.util.ClassUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.server.ResourceFinder;
import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.FeatureContext;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author icode
 */
public class ModelManager extends AmebaFeature {

    private static Logger logger = LoggerFactory.getLogger(ModelManager.class);

    public static final String MODULE_MODELS_KEY_PREFIX = "db.default.models.";
    private static String DEFAULT_DB_NAME = null;

    public static String getDefaultDBName() {
        return DEFAULT_DB_NAME;
    }

    private static Map<String, Set<Class>> modelMap = Maps.newLinkedHashMap();

    public static Set<Class> getModels(String name) {
        return modelMap.get(name);
    }

    @Override
    public boolean configure(FeatureContext context) {
        modelMap.clear();

        Configuration config = context.getConfiguration();
        DEFAULT_DB_NAME = (String) config.getProperty("db.default");

        if (StringUtils.isBlank(DEFAULT_DB_NAME)) {
            DEFAULT_DB_NAME = Model.DB_DEFAULT_SERVER_NAME;
        } else {
            DEFAULT_DB_NAME = StringUtils.deleteWhitespace(DEFAULT_DB_NAME).split(",")[0];
        }


        Set<String> defaultModelsPkg = Sets.newLinkedHashSet();
        //db.default.models.pkg=
        for (String key : config.getPropertyNames()) {
            if (key.startsWith(MODULE_MODELS_KEY_PREFIX)) {
                String modelPackages = (String) config.getProperty(key);
                if (StringUtils.isNotBlank(modelPackages)) {
                    Collections.addAll(defaultModelsPkg, StringUtils.deleteWhitespace(modelPackages).split(","));
                }
            }
        }

        for (String name : DataSourceFeature.getDataSourceNames()) {
            String modelPackages = (String) config.getProperty("db." + name + ".models");
            if (StringUtils.isNotBlank(modelPackages)) {
                Set<String> pkgs = Sets.newHashSet(StringUtils.deleteWhitespace(modelPackages).split(","));

                //db.default.models.pkg=
                //db.default.models+=
                if (getDefaultDBName().equalsIgnoreCase(name)) {
                    pkgs.addAll(defaultModelsPkg);
                }
                Set<Class> classes = Sets.newHashSet();
                ResourceFinder scanner = new PackageNamesScanner(pkgs.toArray(new String[pkgs.size()]), true);
                while (scanner.hasNext()) {
                    String fullName = scanner.next();
                    if (!fullName.endsWith(".class")) {
                        continue;
                    }
                    String className = fullName.substring(0, fullName.length() - 6).replace("/", ".");
                    logger.debug("load class : {}", className);
                    try {
                        classes.add(ClassUtils.getClass(className));
                    } catch (ClassNotFoundException e) {
                        throw new AmebaException(e);
                    }
                }
                modelMap.put(name, classes);
                pkgs.clear();
            }
        }

        defaultModelsPkg.clear();

        return true;
    }
}