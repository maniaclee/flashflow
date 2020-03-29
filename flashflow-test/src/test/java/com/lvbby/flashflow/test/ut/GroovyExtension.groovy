package com.lvbby.flashflow.test.ut

import com.lvbby.flashflow.test.CreateOrderActionExtension
class GroovyExtension implements CreateOrderActionExtension{
    @Override
    String getTitle() {
        return "groovyExtension"
    }
}
