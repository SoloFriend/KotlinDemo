package com.syt.gallery

/**
 * 本文件中存放着各种 Kotlin 创建单例的方式
 */

// 1.饿汉
object Singleton1

// 2.懒汉
class Singleton2 private constructor() {
    companion object {
        private var instance: Singleton2? = null
            get() {
                if (field == null) {
                    field = Singleton2()
                }
                return field
            }

        fun get(): Singleton2 {
            //细心的小伙伴肯定发现了，这里不用getInstance作为为方法名，是因为在伴生对象声明时，内部已有getInstance方法，所以只能取其他名字
            return instance!!
        }
    }
}

// 3.懒汉 线程安全
class Singleton3 private constructor() {
    companion object {
        private var instance: Singleton3? = null
            get() {
                if (field == null) {
                    field = Singleton3()
                }
                return field
            }

        @Synchronized
        fun get(): Singleton3 {
            return instance!!
        }
    }
}

// 4.双重校验锁 (委托)
class Singleton4 private constructor() {
    companion object {
//        val instance: Singleton4 by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
//            Singleton4()
//        }
        val instance by lazy { Singleton4() }
    }
}

// 5.双重校验锁 (自实现)
class Singleton5 private constructor() {
    companion object {
        private var INSTANCE: Singleton5? = null
        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Singleton5().also { INSTANCE = it }
            }
    }
}


// 6.静态内部类
class Singleton6 private constructor() {
    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = Singleton6()
    }
}