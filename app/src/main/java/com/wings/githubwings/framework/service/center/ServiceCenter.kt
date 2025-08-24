package com.wings.githubwings.framework.service.center

import kotlinx.coroutines.sync.Mutex
import kotlin.reflect.KClass

object ServiceCenter {
    val serviceMap = mutableMapOf<String, AbsService>()
    val serviceFactories = mutableMapOf<String, () -> AbsService>()
    var _isDebug: Boolean = false

    var isDebug: Boolean = false
        get() = _isDebug
        set(value) {
            field = value
        }

    fun put(name: String, service: AbsService) {
        synchronized(this) {
            serviceMap[name] = service
        }
    }

    fun putLazy(name: String, factory: () -> AbsService) {
        serviceFactories[name] = factory
    }


    inline fun <reified T> getService(): T? {
        return getServiceByName(T::class.qualifiedName!!)
    }

    fun <T> getService(clazz: KClass<*>): T? {
        return getServiceByName(clazz.qualifiedName!!)
    }

    fun <T> getServiceByName(clazzName: String): T? {
        synchronized(this) {
            if (serviceMap.containsKey(clazzName)) {
                return serviceMap[clazzName]!! as T
            }
            // 尝试通过工厂函数创建实例
            if (serviceFactories.containsKey(clazzName)) {
                val instance = serviceFactories[clazzName]!!()
                serviceMap[clazzName] = instance
                return instance as T
            }
            if (_isDebug) {
                throw Exception("未注册服务：$clazzName")
            }
            return null
        }


    }
}
