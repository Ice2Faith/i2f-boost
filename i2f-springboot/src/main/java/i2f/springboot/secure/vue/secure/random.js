const Random = {
  nextDouble() {
    return Math.random();
  },
  nextInt() {
    return Math.floor(this.nextDouble() * 0x7fff);
  },
  nextLowerInt(val) {
    return this.nextInt() % val;
  },
  nextBetweenInt(min, max) {
    return this.nextInt() % (max - min) + min;
  }
}

export default Random;
