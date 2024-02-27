package i2f.stream.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/2/23 15:00
 * @desc
 */
public class Reference<T> {
    private static enum ObjectState{
        NORMAL,
        NOP,
        EMPTY,
        FINISH;
    }
    public static class NoSuchValueException extends RuntimeException {
        public NoSuchValueException() {
        }

        public NoSuchValueException(String message) {
            super(message);
        }

        public NoSuchValueException(String message, Throwable cause) {
            super(message, cause);
        }

        public NoSuchValueException(Throwable cause) {
            super(cause);
        }

    }
    protected T value;
    protected ObjectState state;

    public static final Reference<?> NOP=new Reference<>(ObjectState.NOP,null);
    public static final Reference<?> EMPTY=new Reference<>(ObjectState.EMPTY,null);
    public static final Reference<?> FINISH=new Reference<>(ObjectState.FINISH,null);

    private Reference(){

    }
    private Reference(ObjectState state, T value){
        this.state=state;
        this.value=value;
    }

    public static<T> Reference<T> nop(){
        return (Reference<T>)NOP;
    }

    public static<T> Reference<T> empty(){
        return (Reference<T>)EMPTY;
    }

    public static<T> Reference<T> finish(){
        return (Reference<T>)FINISH;
    }

    public static<T> Reference<T> of(T value){
        if(value==null){
            return empty();
        }
        return new Reference<>(ObjectState.NORMAL,value);
    }

    public T get(){
        if(this.state==ObjectState.NOP){
            throw new NoSuchValueException("current is nop,means not any thing in here");
        }
        if(this.state==ObjectState.FINISH){
            throw new NoSuchValueException("current is finish, means not any thing in here");
        }
        return this.value;
    }

    public boolean hasValue(){
        if(this.state==ObjectState.NOP){
            return false;
        }
        if(this.state==ObjectState.FINISH){
            return false;
        }
        return true;
    }

    public boolean isPresent(){
        boolean ok = hasValue();
        if(!ok){
            return false;
        }
        return this.state==ObjectState.NORMAL;
    }

    public void ifPresent(Consumer<T> consumer){
        if(isPresent()){
            consumer.accept(this.value);
        }
    }

    public Optional<T> optional(){
        T elem = get();
        return Optional.ofNullable(elem);
    }

    public ObjectState state(){
        return this.state;
    }

    public boolean isNop(){
        return this.state==ObjectState.NOP;
    }

    public boolean isFinish(){
        return this.state==ObjectState.FINISH;
    }

    public boolean isEmpty(){
        return this.state==ObjectState.EMPTY;
    }

    public boolean isNormal(){
        return this.state==ObjectState.NORMAL;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Reference)) {
            return false;
        }

        Reference<?> other = (Reference<?>) obj;
        return Objects.equals(state, other.state)
               && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int h = Objects.hashCode(state);
        int l =  Objects.hashCode(value);
        return h | l;
    }

    @Override
    public String toString() {
        String simpleName = this.getClass().getSimpleName();
        if(isFinish()){
            return simpleName+".finish";
        }
        if(isNop()){
            return simpleName+".nop";
        }
        if(isEmpty()){
            return simpleName+".empty";
        }
        return simpleName+"["+value+"]";
    }
}
