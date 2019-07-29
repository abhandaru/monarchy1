package monarchy.game

trait PieceConf {
  def name: String
  def maxHealth: Int
  def maxWait: Int
  def power: Int
  def armor: Double
  def blocking: Double

  def moveRange: Int
  def movesAside: Boolean = true
  def teleports: Boolean = false

  def attackPatterns: AttackPatterns
  def effectArea: EffectArea
  def blockable: Boolean = true
}

case object Assassin extends PieceConf {
  import DeltaGenerator._
  val name = "Knight of Shadow"
  val maxHealth = 35
  val maxWait = 1
  val power = 18
  val armor = 0.12
  val blocking = 0.7
  val moveRange = 4
  val attackPatterns = SimpleAttackPatterns(AdjecentDeltas)
  val effectArea = new EffectArea {
    override def effects(p0: Vec, ap: AttackPattern): Set[Effect] = {
      AdjecentDeltas.map(Attack(_, power))
    }
  }
}

case object Knight extends PieceConf {
  import DeltaGenerator._
  val name = "Knight of Blade"
  val maxHealth = 50
  val maxWait = 1
  val power = 22
  val armor = 0.25
  val blocking = 0.8
  val moveRange = 3
  val attackPatterns = SimpleAttackPatterns(AdjecentDeltas)
  val effectArea = UniformAttackArea(NoDelta, power)
}

case object Scout extends PieceConf {
  import DeltaGenerator._
  val name = "Knight of Bow"
  val maxHealth = 40
  val maxWait = 2
  val power = 18
  val armor = 0.08
  val blocking = 0.6
  val moveRange = 4
  val attackPatterns = SimpleAttackPatterns(rangeDeltasNoCenter(6))
  val effectArea = UniformAttackArea(NoDelta, power)
}

case object Witch extends PieceConf {
  import DeltaGenerator._
  val name = "Avatar of Woe"
  val maxHealth = 28
  val maxWait = 3
  val power = 24
  val armor = 0.0
  val blocking = 0.2
  val moveRange = 3

  override val blockable = false
  val attackPatterns = new AttackPatterns {
    val max = 4
    val directions = Set(Vec(1, 0), Vec(-1, 0), Vec(0, 1), Vec(0, -1))
    override val patterns: Set[AttackPattern] = {
      directions.map { dir =>
        AttackPattern((1 to max).map(dir * _).toSet)
      }
    }
  }

  val effectArea = new EffectArea {
    override def effects(p0: Vec, ap: AttackPattern): Set[Effect] = {
      ap.points(p0).map(Attack(_, power))
    }
  }
}

case object Pyromancer extends PieceConf {
  import DeltaGenerator._
  val name = "Avatar of Rage"
  val maxHealth = 30
  val maxWait = 3
  val power = 16
  val armor = 0.0
  val blocking = 0.3
  val moveRange = 3
  val attackPatterns = SimpleAttackPatterns(rangeDeltas(3))
  val effectArea = UniformAttackArea(rangeDeltas(1), power)
  override val blockable = false
}

case object MudGolem extends PieceConf {
  import DeltaGenerator._
  val name = "Shadow Sentinel"
  val maxHealth = 60
  val maxWait = 2
  val power = 20
  val armor = 0.0
  val blocking = 0.0
  val moveRange = 5
  val attackPatterns = SimpleAttackPatterns(AdjecentDeltas)
  val effectArea = UniformAttackArea(NoDelta, power)
  override val teleports = true
}

case object Furgon extends PieceConf {
  import DeltaGenerator._
  val name = "Grove Sentinel"
  val maxHealth = 48
  val maxWait = 2
  val power = 0
  val armor = 0.0
  val blocking = 0.5
  val moveRange = 3
  val attackPatterns = SimpleAttackPatterns(rangeDeltas(2))
  val effectArea = new EffectArea {
    val deltas = rangeDeltas(1)
    override def effects(p0: Vec, ap: AttackPattern): Set[Effect] = {
      val pointSet = for {
        p <- ap.points(p0)
        d <- deltas
      } yield p + d
      val pointSetWithoutOrigin = pointSet - p0
      pointSetWithoutOrigin.map(GrowPlant(_))
    }
  }
}

case object Cleric extends PieceConf {
  import DeltaGenerator._
  val name = "High Priestess"
  val maxHealth = 24
  val maxWait = 5
  val power = 12
  val armor = 0.0
  val blocking = 0.0
  val moveRange = 3
  val attackPatterns = SimpleAttackPatterns(NoDelta)
  val effectArea = new EffectArea {
    val deltas = rangeDeltas(1)
    override def effects(p0: Vec, ap: AttackPattern): Set[Effect] = {
      Set(HealAll(power))
    }
  }
}
