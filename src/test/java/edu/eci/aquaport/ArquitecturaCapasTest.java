package edu.eci.aquaport;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaCall.Predicates.target;
import static com.tngtech.archunit.core.domain.properties.HasOwner.Predicates.With.owner;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "edu.eci.aquaport", importOptions = ImportOption.DoNotIncludeTests.class)
class ArquitecturaCapasTest {

    private static final String DOMINIO = "edu.eci.aquaport.dominio..";
    private static final String APLICACION = "edu.eci.aquaport.aplicacion..";
    private static final String INFRAESTRUCTURA = "edu.eci.aquaport.infraestructura..";

    @ArchTest
    static final ArchRule capasRespetanDireccionDeDependencias = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Dominio").definedBy(DOMINIO)
            .layer("Aplicacion").definedBy(APLICACION)
            .layer("Infraestructura").definedBy(INFRAESTRUCTURA)
            .whereLayer("Infraestructura").mayNotBeAccessedByAnyLayer()
            .whereLayer("Aplicacion").mayOnlyBeAccessedByLayers("Infraestructura")
            .whereLayer("Dominio").mayOnlyBeAccessedByLayers("Aplicacion", "Infraestructura");

    @ArchTest
    static final ArchRule dominioSoloUsaJavaEstandar = classes()
            .that().resideInAPackage(DOMINIO)
            .should().onlyDependOnClassesThat().resideInAnyPackage(DOMINIO, "java..");

    @ArchTest
    static final ArchRule dominioNoCreaInfraestructura = noClasses()
            .that().resideInAPackage(DOMINIO)
            .should().callConstructorWhere(target(owner(resideInAPackage(INFRAESTRUCTURA))));

    @ArchTest
    static final ArchRule dependenciasDePuertosSonFinales = fields()
            .that().haveRawType(DescribedPredicate.describe("un puerto del dominio",
                    (JavaClass tipo) -> tipo.getPackageName().endsWith("dominio.puerto")))
            .and().areDeclaredInClassesThat().resideInAnyPackage(DOMINIO, APLICACION)
            .should().beFinal();
}
