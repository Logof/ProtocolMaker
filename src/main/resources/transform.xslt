<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
					   xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
					   xmlns:bpmn = "http://www.omg.org/spec/BPMN/20100524/MODEL"
					   xmlns:activiti="http://activiti.org/bpmn">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	
	<xsl:template match="/bpmn:definitions/bpmn:process">
		{
			<xsl:apply-templates select="bpmn:*" />
			<xsl:text>\n</xsl:text>
		}
	</xsl:template>

	<xsl:template match="bpmn:extensionElements/bpmn:monitoringGroup">
		<xsl:value-of select="@id"/> label="
		<xsl:for-each select="bpmn:monitoringVariable">
			<xsl:value-of select="@content"/>
			<xsl:text>&#xa;&#xd;</xsl:text>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="bpmn:extensionElements/bpmn:activityEffect">
		<xsl:value-of select="@id"/>label="
			<xsl:for-each select="bpmn:activityEffectRule">
				<xsl:value-of select="@rule"/>
				<xsl:text>&lt;br/&gt;</xsl:text>
			</xsl:for-each>"
	</xsl:template>

	<xsl:template match="bpmn:extensionElements/bpmn:timeExpected">
		<xsl:value-of select="@id"/>label="
		<xsl:text>&lt;br/&gt;</xsl:text>
		<xsl:text>&lt;br/&gt;</xsl:text>
		<xsl:text>&lt;br/&gt;</xsl:text>
		<xsl:text>&lt;br/&gt;</xsl:text>
		<xsl:text>&lt;br/&gt;</xsl:text>
		<xsl:for-each select="bpmn:param">
			<xsl:value-of select="@name"/>:<xsl:value-of select="@value"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@unit"/>
			<xsl:text>&lt;br/&gt;</xsl:text>
		</xsl:for-each>"
	</xsl:template>
	
	<xsl:template match="bpmn:extensionElements/bpmn:decisionQuestion">
		<xsl:value-of select="@id"/>
		<xsl:choose>
			<xsl:when test="@type='quality'">"stereotype_decisionQuestionQuality.png"</xsl:when>
			<xsl:otherwise>"stereotype_decisionQuestionNormal.png"</xsl:otherwise>
		</xsl:choose>, label="
		<xsl:text>&lt;br/&gt;</xsl:text>
		<xsl:text>&lt;br/&gt;</xsl:text>
		<xsl:text>&lt;br/&gt;</xsl:text>
		<xsl:text>&lt;br/&gt;</xsl:text>
		<xsl:value-of select="@condition"/>
	</xsl:template>

	
	<xsl:template match="bpmn:extensionElements/bpmn:association">
		<xsl:value-of select="@sourceRef"/> -> <xsl:value-of select="@targetRef"/>
		<xsl:value-of select="@sourceRef"/><xsl:text> </xsl:text><xsl:value-of select="@targetRef"/> };
	</xsl:template>

	<xsl:template match="bpmn:sequenceFlow">
		<xsl:variable name="sourceRef" select="@sourceRef"/>
		<xsl:variable name="id" select="@id"/>
		<xsl:choose>
			<xsl:when test="//bpmn:extensionElements/bpmn:association[@sourceRef=$id]">
				<xsl:value-of select="$id"/>
				<xsl:value-of select="@sourceRef"/> -> <xsl:value-of select="$id"/>
				<xsl:value-of select="$id"/> -> <xsl:value-of select="@targetRef"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="@sourceRef"/> -> <xsl:value-of select="@targetRef"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	

	<xsl:template match="bpmn:exclusiveGateway">
		<xsl:value-of select="@id"/>
		<xsl:text>&lt;br/&gt;</xsl:text>
	</xsl:template>
	
	<xsl:template match="bpmn:startEvent">
		<xsl:value-of select="@id"/>
		<xsl:text>&lt;br/&gt;</xsl:text>
	</xsl:template>

	<xsl:template match="bpmn:endEvent">
		<xsl:value-of select="@id"/>
		<xsl:text>&lt;br/&gt;</xsl:text>
	</xsl:template>

	<xsl:template match="bpmn:task">
		<xsl:value-of select="@id"/>
		<xsl:text>&lt;br/&gt;</xsl:text>
	</xsl:template>
</xsl:stylesheet>
