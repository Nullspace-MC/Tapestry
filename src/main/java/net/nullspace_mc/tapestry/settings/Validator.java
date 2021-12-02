package net.nullspace_mc.tapestry.settings;

/**
 * Abstract class enclosing a rule's validator method
 */
abstract class Validator<T> {
    abstract boolean validate(T value);
}
